/*
 * Copyright (c) 2020  DemonScythe45
 *
 * This file is part of EntityThreading
 *
 *     EntityThreading is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation; version 3 only
 *
 *     EntityThreading is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with EntityThreading.  If not, see <https://www.gnu.org/licenses/>
 */

package demonscythe.entitythreading.transform;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ASM4;

public class WorldMethodTransformer extends MethodVisitor {
    private boolean updatedReference = false;
    private boolean addBlockMethod = false;

    WorldMethodTransformer(MethodVisitor methodVisitor) {
        super(ASM4, methodVisitor);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        //System.out.println("Visiting line: " + line);
        if (line == 1797) addBlockMethod = true;
        super.visitLineNumber(line, start);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        //System.out.println("VARINS: " + opcode + " " + var);
        if (addBlockMethod) {
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "demonscythe/entitythreading/schedule/EntityTickScheduler", "waitForFinish", "()V", false);
            addBlockMethod = false;
        }
        super.visitVarInsn(opcode, var);
    }

    /**
     * @Override public void visitLdcInsn(Object cst) {
     * System.out.println("Constant: " + cst);
     * super.visitLdcInsn(cst);
     * }
     **/

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        //System.out.println("Opcode: " + opcode + " Owner: " + owner + " Name: " + name + " Desc: " + desc);
        if (
                (opcode == Opcodes.INVOKEVIRTUAL) &&
                        (owner.equals("net/minecraftforge/server/timings/TimeTracker")) &&
                        (name.equals("trackStart")) &&
                        (desc.equals("(Ljava/lang/Object;)V"))
        ) {
            if (!updatedReference) {
                System.out.println("Found the first timing tracker!");
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        } else if (
                (opcode == Opcodes.INVOKEVIRTUAL) &&
                        (owner.equals("amu")) &&
                        (name.equals("h")) &&
                        (desc.equals("(Lvg;)V"))
        ) {
            System.out.println("Found method to replace!");
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "demonscythe/entitythreading/schedule/EntityTickScheduler", "queueEntity", "(Lamu;Lvg;)V", false);
            updatedReference = true;
        } else {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}
