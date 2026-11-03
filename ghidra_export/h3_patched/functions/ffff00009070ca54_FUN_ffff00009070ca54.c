// ffff00009070ca54  size=236  FUN_ffff00009070ca54

undefined8 FUN_ffff00009070ca54(long param_1,uint *param_2)

{
  long lVar1;
  int iVar2;
  undefined8 uVar3;
  long local_50;
  ulong local_48;
  long local_40;
  ulong local_38;
  
  local_40 = 0;
  local_38 = 0;
  local_48 = (ulong)*param_2;
  local_50 = param_1;
  FUN_ffff00009077eb88(&local_40,0,0x10);
  iVar2 = thunk_FUN_ffff0000907b67a8(&local_50);
  uVar3 = 0;
  if ((iVar2 == 0) && (uVar3 = thunk_FUN_ffff0000907b6820(1,&local_50,&local_38), (int)uVar3 == 0))
  {
    lVar1 = param_1 + 0x300000;
    local_40 = lVar1;
    uVar3 = thunk_FUN_ffff0000907b68b8(1,&local_50,&local_40);
    if (*(int *)(local_40 + 0x10) == 0x800) {
      *(undefined4 *)(local_40 + 0x10) = 0x200;
    }
    if ((int)uVar3 == 0) {
      FUN_ffff00009077e858(lVar1 + (local_38 & 0xffffffff),param_1,*param_2);
      *param_2 = *param_2 + (int)local_38;
      FUN_ffff00009077e858(param_1,lVar1);
      uVar3 = 0;
    }
  }
  return uVar3;
}


