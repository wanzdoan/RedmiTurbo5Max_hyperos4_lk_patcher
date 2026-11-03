// ffff0000907c5c0c  size=152  FUN_ffff0000907c5c0c

undefined8 FUN_ffff0000907c5c0c(undefined8 param_1,long param_2,long param_3,ulong param_4)

{
  int iVar1;
  undefined8 uVar2;
  long local_28;
  
  uVar2 = 2;
  local_28 = 0;
  if (((param_2 != 0) && (param_3 != 0)) && (0xf < param_4)) {
    iVar1 = thunk_FUN_ffff000090735d54(param_2,&local_28);
    if (iVar1 == 0) {
      if (local_28 == 0) {
        uVar2 = 2;
      }
      else {
        FUN_ffff00009077ee50(param_3,0,param_4);
        FUN_ffff00009077eb20(param_3,local_28,0x25);
        uVar2 = 0;
      }
    }
    else {
      uVar2 = 3;
    }
  }
  return uVar2;
}


