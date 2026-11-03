// ffff0000907c5598  size=152  FUN_ffff0000907c5598

undefined8 FUN_ffff0000907c5598(undefined8 param_1,long param_2,long param_3,ulong param_4)

{
  int iVar1;
  undefined8 uVar2;
  long local_28;
  
  uVar2 = 2;
  local_28 = 0;
  if (((param_2 != 0) && (param_3 != 0)) && (0xf < param_4)) {
    iVar1 = thunk_FUN_ffff000090735abc(param_2,&local_28);
    if (iVar1 == 0) {
      if (local_28 == 0) {
        uVar2 = 2;
      }
      else {
        FUN_ffff00009077eb88(param_3,0,param_4);
        FUN_ffff00009077e858(param_3,local_28,0x25);
        uVar2 = 0;
      }
    }
    else {
      uVar2 = 3;
    }
  }
  return uVar2;
}


