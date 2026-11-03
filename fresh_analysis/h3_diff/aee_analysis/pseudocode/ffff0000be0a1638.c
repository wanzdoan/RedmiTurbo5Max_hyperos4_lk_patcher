
int FUN_ffff0000be0a1638(long param_1,long param_2,long param_3,byte param_4,uint param_5)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  ulong uVar4;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  if (param_4 != 0) {
    uVar3 = (ulong)(param_5 >> 1 & 0x3f);
    uVar4 = (ulong)param_4;
    do {
      iVar2 = FUN_ffff0000be09c06c(param_2,param_3,uVar3 == 0);
      if (iVar2 != 0) {
        return iVar2;
      }
      iVar2 = FUN_ffff0000be09c06c(param_2 + 0x18,param_3 + 0x18,uVar3 == 0);
      if (iVar2 != 0) {
        return iVar2;
      }
      param_3 = param_3 + 0x48;
      uVar3 = uVar3 - 1;
      uVar4 = uVar4 - 1;
    } while (uVar4 != 0);
  }
  local_70 = 0;
  uStack_68 = 0;
  local_78 = 0;
  FUN_ffff0000be09bcf0(&local_78);
  param_2 = param_2 + 0x18;
  iVar2 = FUN_ffff0000be09d008(&local_78,param_1 + 8,param_2);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff0000be09c9e4(param_2,0);
    uVar1 = 0;
    if (iVar2 != 0) {
      uVar1 = param_5 >> 7 & 1;
    }
    iVar2 = FUN_ffff0000be09c06c(param_2,&local_78,uVar1);
  }
  FUN_ffff0000be09bd08(&local_78);
  return iVar2;
}


