
undefined8 FUN_ffff0000907bbbdc(uint *param_1,uint param_2,uint param_3,uint param_4,uint param_5)

{
  uint uVar1;
  bool bVar2;
  bool bVar3;
  bool bVar4;
  bool bVar5;
  bool bVar6;
  long lVar7;
  ulong uVar8;
  ulong uVar9;
  
  lVar7 = FUN_ffff0000907168b0();
  uVar9 = lVar7 + (ulong)param_4 * 1000;
  uVar8 = FUN_ffff0000907168b0();
  if (uVar8 <= uVar9) {
    bVar5 = false;
    bVar4 = false;
    bVar3 = false;
    bVar2 = false;
    do {
      uVar1 = *param_1;
      if ((bVar2) || (uVar8 = FUN_ffff0000907168b0(), uVar8 <= lVar7 + 1000000U)) {
        if ((!bVar3) && (uVar8 = FUN_ffff0000907168b0(), lVar7 + 2000000U < uVar8)) {
          bVar3 = true;
          goto LAB_ffff0000907bbd10;
        }
        if ((!bVar4) && (uVar8 = FUN_ffff0000907168b0(), lVar7 + 3000000U < uVar8)) {
          bVar4 = true;
          goto LAB_ffff0000907bbd10;
        }
        if (bVar5) {
          bVar5 = true;
        }
        else {
          uVar8 = FUN_ffff0000907168b0();
          if (lVar7 + 4000000U < uVar8) {
            bVar5 = true;
            goto LAB_ffff0000907bbd10;
          }
          bVar5 = false;
        }
      }
      else {
        bVar2 = true;
LAB_ffff0000907bbd10:
        FUN_ffff0000907b83c0(param_1);
        FUN_ffff0000907b83f0(param_1);
      }
      bVar6 = (uVar1 & param_2) == param_3;
      if ((param_5 & 1) == 0) {
        if (!bVar6) {
          return 0;
        }
      }
      else if (bVar6) {
        return 0;
      }
      uVar8 = FUN_ffff0000907168b0();
    } while (uVar8 <= uVar9);
  }
  FUN_ffff0000907b83c0(param_1);
  FUN_ffff0000907b83f0(param_1);
  return 0x7249;
}


