// File offset entry: 0x3bc0

undefined8 FUN_02003ac0(long param_1,ulong param_2,ulong param_3,long param_4,undefined4 param_5)

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
  uVar8 = (ulong)uVar2;
  lVar1 = 0x103180;
  if (*(long *)(param_1 + 0x18) != 0) {
    lVar1 = *(long *)(param_1 + 0x18);
  }
  if (uVar2 == 0) {
LAB_02003888:
    uVar5 = 0xffffffff;
  }
  else {
    if (param_3 != 0) {
      uVar10 = uVar8 - 1 & param_2;
      uVar9 = -uVar8;
      uVar7 = param_3;
      uVar6 = param_2;
      if (uVar10 != 0) {
        uVar6 = 0;
        if (uVar8 != 0) {
          uVar6 = (uVar9 & param_2) / uVar8;
        }
        iVar4 = (**(code **)(param_1 + 0x30))(param_1,uVar6,1,lVar1,param_5);
        if (iVar4 != 0) goto LAB_02003888;
        uVar6 = uVar8 - uVar10;
        if (uVar10 + param_3 <= uVar8) {
          uVar6 = param_3;
        }
        func_0x020915ac(param_4,lVar1 + uVar10,uVar6);
        param_4 = param_4 + uVar6;
        uVar7 = param_3 - uVar6;
        uVar6 = uVar6 + param_2;
      }
      param_3 = param_3 + param_2;
      if (uVar8 <= uVar7) {
        uVar10 = 0;
        if (uVar8 != 0) {
          uVar10 = ((param_3 & uVar9) - (uVar6 & uVar9)) / uVar8;
        }
        if ((int)uVar10 != 0) {
          uVar3 = 0;
          if (uVar8 != 0) {
            uVar3 = (uVar6 & uVar9) / uVar8;
          }
          iVar4 = (**(code **)(param_1 + 0x30))(param_1,uVar3,uVar10 & 0xffffffff,param_4,param_5);
          if (iVar4 != 0) goto LAB_02003888;
        }
        uVar10 = (ulong)(uVar2 * (int)uVar10);
        uVar6 = uVar6 + uVar10;
        param_4 = param_4 + uVar10;
        uVar7 = uVar7 - uVar10;
      }
      if (param_3 <= uVar6) {
        return 0;
      }
      uVar6 = uVar8 - 1 & param_3;
      if (uVar6 == 0) {
        return 0;
      }
      if (uVar7 == 0) {
        return 0;
      }
      uVar7 = 0;
      if (uVar8 != 0) {
        uVar7 = (param_3 & uVar9) / uVar8;
      }
      iVar4 = (**(code **)(param_1 + 0x30))(param_1,uVar7,1,lVar1,param_5);
      if (iVar4 != 0) goto LAB_02003888;
      func_0x020915ac(param_4,lVar1,uVar6);
    }
    uVar5 = 0;
  }
  return uVar5;
}


