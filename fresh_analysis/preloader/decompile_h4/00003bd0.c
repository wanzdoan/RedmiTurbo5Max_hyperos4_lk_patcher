// File offset entry: 0x3bd0

undefined8 FUN_02003ad0(long param_1,ulong param_2,ulong param_3,long param_4,undefined4 param_5)

{
  long lVar1;
  uint uVar2;
  ulong uVar3;
  int iVar4;
  undefined8 uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  
  param_3 = param_3 & 0xffffffff;
  if (param_1 == 0) {
    return 0xffffffff;
  }
  uVar2 = *(uint *)(param_1 + 4);
  uVar9 = (ulong)uVar2;
  lVar1 = 0x103180;
  if (*(long *)(param_1 + 0x18) != 0) {
    lVar1 = *(long *)(param_1 + 0x18);
  }
  if (uVar2 == 0) goto LAB_02003a90;
  if (param_3 == 0) {
LAB_02003a98:
    uVar5 = 0;
  }
  else {
    uVar6 = uVar9 - 1 & param_2;
    uVar10 = -uVar9;
    uVar7 = param_3;
    uVar8 = param_2;
    if (uVar6 == 0) {
LAB_020039c8:
      param_3 = param_3 + param_2;
      if (uVar9 <= uVar7) {
        uVar6 = 0;
        if (uVar9 != 0) {
          uVar6 = ((param_3 & uVar10) - (uVar8 & uVar10)) / uVar9;
        }
        if ((int)uVar6 != 0) {
          uVar3 = 0;
          if (uVar9 != 0) {
            uVar3 = (uVar8 & uVar10) / uVar9;
          }
          iVar4 = (**(code **)(param_1 + 0x38))(param_1,uVar3,uVar6 & 0xffffffff,param_4,param_5);
          if (iVar4 != 0) goto LAB_02003a90;
        }
        uVar6 = (ulong)(uVar2 * (int)uVar6);
        param_4 = param_4 + uVar6;
        uVar8 = uVar8 + uVar6;
        uVar7 = uVar7 - uVar6;
      }
      uVar6 = uVar9 - 1 & param_3;
      if (((uVar6 == 0) || (uVar7 == 0)) || (param_3 <= uVar8)) goto LAB_02003a98;
      uVar8 = 0;
      if (uVar9 != 0) {
        uVar8 = (param_3 & uVar10) / uVar9;
      }
      iVar4 = (**(code **)(param_1 + 0x30))(param_1,uVar8 & 0xffffffff,1,lVar1,param_5);
      if (iVar4 == 0) {
        FUN_020915ac(lVar1,param_4,uVar6);
        iVar4 = (**(code **)(param_1 + 0x38))(param_1,uVar8 & 0xffffffff,1,lVar1,param_5);
        if (iVar4 == 0) goto LAB_02003a98;
      }
    }
    else {
      uVar8 = 0;
      if (uVar9 != 0) {
        uVar8 = (uVar10 & param_2) / uVar9;
      }
      uVar3 = uVar9 - uVar6;
      if (uVar6 + param_3 <= uVar9) {
        uVar3 = param_3;
      }
      iVar4 = (**(code **)(param_1 + 0x30))(param_1,uVar8 & 0xffffffff,1,lVar1,param_5);
      if (iVar4 == 0) {
        FUN_020915ac(lVar1 + uVar6,param_4,uVar3);
        iVar4 = (**(code **)(param_1 + 0x38))(param_1,uVar8 & 0xffffffff,1,lVar1,param_5);
        if (iVar4 == 0) {
          param_4 = param_4 + uVar3;
          uVar7 = param_3 - uVar3;
          uVar8 = uVar3 + param_2;
          goto LAB_020039c8;
        }
      }
    }
LAB_02003a90:
    uVar5 = 0xffffffff;
  }
  return uVar5;
}


