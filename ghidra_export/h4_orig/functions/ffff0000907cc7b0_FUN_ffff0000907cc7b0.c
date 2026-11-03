// ffff0000907cc7b0  size=472  FUN_ffff0000907cc7b0

int FUN_ffff0000907cc7b0(long param_1,long param_2,undefined8 param_3,undefined8 param_4)

{
  int iVar1;
  long lVar2;
  int iVar3;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  lVar2 = *(long *)(param_1 + 0xb0);
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  FUN_ffff0000907c7040(&uStack_68);
  FUN_ffff0000907c7040(&local_80);
  iVar3 = 0;
  do {
    iVar1 = FUN_ffff0000907c9f00(&uStack_68,lVar2 + 7U >> 3,param_3,param_4);
    while( true ) {
      if (iVar1 != 0) goto LAB_ffff0000907cc94c;
      iVar1 = FUN_ffff0000907c7c44(&uStack_68,param_1 + 8);
      if (iVar1 < 0) break;
      iVar1 = FUN_ffff0000907c7ae8(&uStack_68,1);
    }
    if (iVar3 == 0xb) {
      return -0x4d00;
    }
    iVar3 = iVar3 + 1;
    iVar1 = FUN_ffff0000907c7d34(&uStack_68,1);
  } while (iVar1 < 1);
  lVar2 = param_2 + 0x30;
  iVar1 = FUN_ffff0000907c846c(lVar2,lVar2,&uStack_68);
  if (((((iVar1 == 0) && (iVar1 = FUN_ffff0000907cc6c4(lVar2,param_1), iVar1 == 0)) &&
       (iVar1 = FUN_ffff0000907c846c(&local_80,&uStack_68,&uStack_68), iVar1 == 0)) &&
      ((iVar1 = FUN_ffff0000907cc6c4(&local_80,param_1), iVar1 == 0 &&
       (iVar1 = FUN_ffff0000907c846c(param_2,param_2,&local_80), iVar1 == 0)))) &&
     ((iVar1 = FUN_ffff0000907cc6c4(param_2,param_1), iVar1 == 0 &&
      ((iVar1 = FUN_ffff0000907c846c(&local_80,&local_80,&uStack_68), iVar1 == 0 &&
       (iVar1 = FUN_ffff0000907cc6c4(&local_80,param_1), iVar1 == 0)))))) {
    param_2 = param_2 + 0x18;
    iVar1 = FUN_ffff0000907c846c(param_2,param_2,&local_80);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000907cc6c4(param_2,param_1);
    }
  }
LAB_ffff0000907cc94c:
  FUN_ffff0000907c7058(&uStack_68);
  FUN_ffff0000907c7058(&local_80);
  return iVar1;
}


