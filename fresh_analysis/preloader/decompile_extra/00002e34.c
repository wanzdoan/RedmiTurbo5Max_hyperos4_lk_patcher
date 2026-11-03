// File offset entry: 0x2e34

undefined8
FUN_02002d34(ulong param_1,ulong param_2,ulong param_3,ulong param_4,ulong param_5,ulong param_6,
            ulong param_7,ulong param_8)

{
  ulong uVar1;
  ulong uVar2;
  ulong uVar3;
  uint uVar4;
  int iVar5;
  undefined8 uVar6;
  long lVar7;
  ulong uVar8;
  ulong uVar9;
  uint uVar10;
  ulong uVar11;
  uint uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  ulong uVar17;
  ulong uVar18;
  long unaff_x28;
  undefined1 auVar19 [16];
  ulong uStack_70;
  long lStack_68;
  
  lStack_68 = *plRam0000000002103280;
  uVar13 = param_7;
  if (((param_3 | param_2 | param_4) & (-1L << (param_7 & 0x3f) ^ 0xffffffffffffffffU)) == 0) {
    uVar12 = (uint)param_7;
    uVar10 = (uint)param_6;
    uVar16 = 1L << (param_6 & 0x3f);
    uVar4 = 1 << (ulong)(uVar12 & 0x1f);
    uVar8 = 3;
    if (uVar12 <= uVar10 && uVar10 - uVar12 != 0) {
      uVar8 = 1;
    }
    uVar9 = param_3;
    uVar2 = param_2;
    uVar15 = param_4;
    uVar11 = param_6;
    uVar14 = param_5;
    uVar1 = param_1;
    for (uVar18 = param_4; uVar18 != 0; uVar18 = uVar18 - uVar3) {
      uVar17 = uVar2 & uVar16 - 1;
      uVar14 = uVar16 - uVar17;
      uVar3 = uVar18;
      if (uVar14 <= uVar18) {
        uVar3 = uVar14;
      }
      uVar14 = uVar2 >> (param_6 & 0x3f);
      if ((((param_3 | uVar2) & uVar16 - 1) != 0) || (0x1e < uVar10 || uVar3 != uVar16)) {
        uVar15 = *(ulong *)(param_8 + uVar14 * 8);
        uVar9 = uVar15 & 3;
        if (uVar9 == 0) {
          uStack_70 = 0;
          if (uVar12 == 0xc) {
            lVar7 = func_0x0208a7c4();
            if (lVar7 != 0) {
              uStack_70 = func_0x0208a4e8();
              goto LAB_02002ef8;
            }
          }
          else if (uVar12 < 0xd) {
            unaff_x28 = func_0x02002c38(uVar4,uVar4);
            if (unaff_x28 != 0) {
              uStack_70 = func_0x0206eea0();
              if (uStack_70 == 0) goto LAB_02002fac;
              goto LAB_02002ef8;
            }
          }
          else {
            uVar9 = func_0x0208a990((ulong)(uVar4 >> 0xc),param_7,&uStack_70,0);
            if (uVar9 == uVar4 >> 0xc) {
LAB_02002ef8:
              unaff_x28 = func_0x0206ee94(uStack_70);
              FUN_02091664(unaff_x28,0,uVar4);
              DataMemoryBarrier(2,2);
              *(ulong *)(param_8 + uVar14 * 8) = uStack_70 | 3;
              goto joined_r0x02002f20;
            }
          }
LAB_02002fb4:
          FUN_0208ada0(0x20c257d,0x20c8ae9,0x129);
        }
        else {
          if (uVar9 != 3) {
            if (uVar9 != 1) {
              func_0x0208b29c(0x20b81ec,0x20c9a50,0x141);
LAB_02002fac:
              func_0x02002bf8(unaff_x28);
              goto LAB_02002fb4;
            }
            goto LAB_02002fd0;
          }
          unaff_x28 = func_0x0206ee94(uVar15 & 0xfffffffff000);
joined_r0x02002f20:
          if (unaff_x28 != 0) {
            uVar11 = (ulong)((uVar10 - uVar12) + 3);
            uVar9 = param_3;
            uVar15 = uVar3;
            uVar14 = param_5;
            uVar13 = param_7;
            iVar5 = FUN_02002d34(uVar1,uVar17,param_3,uVar3,param_5,uVar11,param_7,unaff_x28);
            if (iVar5 == 0) goto LAB_02002f50;
          }
        }
LAB_02002fd0:
        uVar9 = param_4 - uVar18;
        func_0x020030dc(param_1,param_2,uVar9,param_6,param_7,param_8);
        DataSynchronizationBarrier(3,3,0);
        uVar6 = 0xffffffff;
        goto code_r0x02002ff4;
      }
      if (*(long *)(param_8 + uVar14 * 8) != 0) {
        FUN_0208ada0(0x20c9a13,0x20b71f8,0x1c9,uVar14);
        goto LAB_02002fd0;
      }
      *(ulong *)(param_8 + uVar14 * 8) = uVar8 | param_5 | param_3;
      DataMemoryBarrier(2,2);
      TLBI_VAE3IS(uVar1 >> 0xc & 0xfffffffffff);
      InstructionSynchronizationBarrier();
      uVar14 = 0;
LAB_02002f50:
      uVar1 = uVar3 + uVar1;
      uVar2 = uVar3 + uVar2;
      param_3 = uVar3 + param_3;
    }
    uVar6 = 0;
    param_6 = uVar15;
    param_7 = uVar14;
    param_8 = uVar11;
  }
  else {
    uVar9 = 0x1ad;
    param_8 = param_6;
    FUN_0208ada0(0x20b624e,0x20b71f8);
    uVar6 = 0xfffffff8;
    param_6 = param_4;
    param_7 = param_5;
  }
code_r0x02002ff4:
  if (lRam0000000002100880 == lStack_68) {
    return uVar6;
  }
  auVar19 = FUN_02091090(uVar6);
  uVar8 = auVar19._8_8_;
  lVar7 = auVar19._0_8_;
  uVar16 = 1L << (param_6 & 0x3f);
  if (uVar16 < uVar8 || uVar16 - uVar8 < lVar7 - uVar9) {
    FUN_0208ada0(0x20be388,0x20b8201,0x212,lVar7,uVar8,uVar9,uVar16);
  }
  else {
    if (uVar13 != 0) {
      func_0x020030dc(lVar7,lVar7 - uVar9,uVar8,param_7 & 0xffffffff,param_8 & 0xffffffff,uVar13);
      DataSynchronizationBarrier(3,3,0);
      return 0;
    }
    FUN_0208ada0(0x20c05ec,0x20b8201,0x217);
  }
  return 0xfffffff8;
}


