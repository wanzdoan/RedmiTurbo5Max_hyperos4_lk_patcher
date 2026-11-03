// File offset entry: 0x6b8f0

/* WARNING: Possible PIC construction at 0x0206b984: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0206b988) */
/* WARNING: Removing unreachable block (ram,0x0206b998) */
/* WARNING: Removing unreachable block (ram,0x0206b9a8) */
/* WARNING: Removing unreachable block (ram,0x0206b9ac) */
/* WARNING: Removing unreachable block (ram,0x0206b9b0) */
/* WARNING: Removing unreachable block (ram,0x0206b9b4) */
/* WARNING: Removing unreachable block (ram,0x0206b9b8) */
/* WARNING: Removing unreachable block (ram,0x0206b98c) */
/* WARNING: Removing unreachable block (ram,0x0206b9d4) */
/* WARNING: Removing unreachable block (ram,0x0206b9f8) */
/* WARNING: Removing unreachable block (ram,0x0206ba08) */
/* WARNING: Removing unreachable block (ram,0x0206ba1c) */
/* WARNING: Removing unreachable block (ram,0x0206ba94) */
/* WARNING: Removing unreachable block (ram,0x0206ba8c) */
/* WARNING: Removing unreachable block (ram,0x0206baa0) */
/* WARNING: Removing unreachable block (ram,0x0206bae8) */
/* WARNING: Removing unreachable block (ram,0x0206bb44) */
/* WARNING: Removing unreachable block (ram,0x0206bb68) */
/* WARNING: Removing unreachable block (ram,0x0206bb08) */
/* WARNING: Removing unreachable block (ram,0x0206bbe4) */
/* WARNING: Removing unreachable block (ram,0x0206bcf4) */

bool FUN_0206b7f0(ulong param_1,ulong param_2,ulong param_3,ulong param_4,uint param_5,long param_6)

{
  long *plVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  bool bVar5;
  ulong uVar6;
  undefined8 uVar7;
  ulong uVar8;
  long lVar9;
  ulong *puVar10;
  undefined1 auVar11 [16];
  ulong uStack_a0;
  
  plVar1 = plRam0000000002103338;
  uStack_a0 = param_2;
  if (param_6 == 0) {
    uVar7 = 0x20cd3cc;
  }
  else {
    if (param_1 != 0) {
      if ((((uint)param_2 | (uint)param_1) & 0xfff) == 0) {
        if ((param_4 & 0xfff) == 0) {
          if ((param_2 & param_2 - 1) == 0) {
            if ((((0x7e < *(uint *)(*plRam0000000002103338 + 0x7cae8)) || (1 < param_5)) ||
                (0x7e < *(uint *)(*plRam0000000002103338 + 0x7d6f8))) ||
               (uVar6 = FUN_02091524(param_6), 0x7e < uVar6)) {
              FUN_0208ada0(0x20c828b,0x20b0842);
              FUN_0208ada0(0x20c4ece);
              lVar9 = *plVar1;
              FUN_0208ada0(0x20ce668,*(undefined4 *)(lVar9 + 0x7cae8),
                           *(undefined4 *)(lVar9 + 0x7d6f8),param_5,param_6);
              goto LAB_0206bd10;
            }
            lVar9 = *plVar1;
            uStack_a0 = 0x1000;
            if (param_2 != 0) {
              uStack_a0 = param_2 & 0xfffffffffffff000;
            }
            puVar10 = (ulong *)(lVar9 + 0x7caf8);
            for (uVar6 = 0; *(uint *)(lVar9 + 0x7cae8) != uVar6; uVar6 = uVar6 + 1) {
              uVar3 = puVar10[-1];
              uVar4 = *puVar10;
              uVar2 = uVar4 + uVar3;
              uVar8 = uVar2;
              if (param_3 <= uVar2) {
                uVar8 = param_3;
              }
              if (param_4 == 0) {
                bVar5 = param_1 <= uVar4;
              }
              else {
                bVar5 = ((param_4 >= uVar3 && param_4 <= uVar2) &&
                        (param_4 < uVar3 || uVar2 != param_4)) && param_1 <= uVar4;
              }
              if (((bVar5 && uVar3 < param_3) && uVar3 < uVar8) && (uVar3 != 0 || param_1 <= uVar8))
              {
                uVar6 = uVar8 - param_1 & -uStack_a0;
                if (param_4 != 0) {
                  uVar6 = param_4;
                }
                goto code_r0x0206bd58;
              }
              puVar10 = puVar10 + 3;
            }
            uVar7 = 0x20b381a;
            goto LAB_0206bcec;
          }
          uVar7 = 0x20c92c1;
        }
        else {
          uVar7 = 0x20cd3ef;
        }
      }
      else {
        uVar7 = 0x20b37e6;
      }
      FUN_0208ada0(uVar7,param_6);
      goto LAB_0206bd10;
    }
    uVar7 = 0x20cb4e5;
  }
LAB_0206bcec:
  FUN_0208ada0(uVar7);
LAB_0206bd10:
  FUN_0208ada0(0x20c828b,0x20b0842);
  FUN_0208ada0(0x20cb4ff,param_1,param_5,param_6,0,param_3,uStack_a0,param_4);
  func_0x0206be74();
  uVar7 = func_0x0206cad4();
  auVar11 = FUN_0208b224(uVar7,0x211);
  param_1 = auVar11._8_8_;
  uVar6 = auVar11._0_8_;
code_r0x0206bd58:
  uVar8 = (ulong)*(uint *)(*plRam0000000002103338 + 0x82300);
  uVar2 = param_1 + uVar6;
  plVar1 = (long *)(*plRam0000000002103338 + 0x82310);
  while( true ) {
    if (uVar8 == 0) {
      return false;
    }
    if (((ulong)plVar1[-1] <= uVar6) &&
       (uVar3 = *plVar1 + plVar1[-1], uVar6 < uVar3 && uVar2 <= uVar3)) break;
    uVar8 = uVar8 - 1;
    plVar1 = plVar1 + 2;
  }
  if (param_1 == 0) {
    return false;
  }
  if (uVar2 < 0x100000001) {
    return false;
  }
  if ((((uVar6 >> 0x20 != 0) && ((uVar6 >> 0x21 != 0 || (uVar2 < 0x200000001)))) &&
      ((uVar6 >> 0x22 != 0 || (uVar2 < 0x400000001)))) &&
     ((uVar6 >> 0x23 != 0 || (uVar2 < 0x800000001)))) {
    bVar5 = uVar6 >> 0x24 != 0;
    return (!bVar5 && 0xfffffffff < uVar2) && (bVar5 || uVar2 != 0x1000000000);
  }
  return true;
}


