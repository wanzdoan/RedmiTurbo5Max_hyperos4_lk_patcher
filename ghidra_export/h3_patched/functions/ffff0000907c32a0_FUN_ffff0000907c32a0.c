// ffff0000907c32a0  size=340  FUN_ffff0000907c32a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907c32a0
              (undefined8 param_1,undefined8 param_2,undefined8 param_3,uint *param_4,
              undefined8 *param_5)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  local_70 = param_5[2];
  uStack_68 = param_5[3];
  local_60 = param_5[4];
  uStack_58 = param_5[5];
  local_80 = *param_5;
  uStack_78 = param_5[1];
  iVar3 = FUN_ffff0000907c3104(param_1,param_3,param_4,&local_80);
  if (iVar3 == -1) {
    return -1;
  }
  uVar2 = iVar3 + (int)param_1;
  if (uVar2 < *param_4) {
    uVar1 = uVar2 + 3;
    if (*param_4 - 1 <= uVar2) {
      uVar1 = 1;
    }
    iVar6 = *(int *)((long)param_5 + 4);
    iVar5 = *(int *)(param_5 + 1);
    iVar4 = FUN_ffff0000907c2fd4
                      ((ulong)param_4[uVar2 + 2] + (long)param_4,param_3,
                       param_4[uVar1] - param_4[uVar2 + 2],*(undefined4 *)(param_5 + 4));
    if ((*(int *)(param_5 + 2) == 0x10e) || (*(int *)(param_5 + 2) == 0x5a)) {
      iVar5 = *(int *)((long)param_5 + 4);
      iVar6 = *(int *)(param_5 + 1);
    }
    if (_DAT_ffff00009090d8a8 == 0) {
      if (iVar4 == iVar5 * iVar6 * 4) {
        _DAT_ffff00009090d8a8 = 0x20;
      }
      else {
        if (iVar4 != iVar5 * iVar6 * 2) goto LAB_ffff0000907c33cc;
        _DAT_ffff00009090d8a8 = 0x10;
      }
    }
    local_70 = param_5[2];
    uStack_68 = param_5[3];
    local_60 = param_5[4];
    uStack_58 = param_5[5];
    local_80 = *param_5;
    uStack_78 = param_5[1];
    FUN_ffff0000907c3f30(param_2,0,CONCAT44(iVar5,iVar6),param_3,&local_80);
  }
  else {
LAB_ffff0000907c33cc:
    iVar3 = -1;
  }
  return iVar3;
}


