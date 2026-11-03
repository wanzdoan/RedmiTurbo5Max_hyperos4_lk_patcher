
int FUN_ffff0000be085c64(long param_1)

{
  int iVar1;
  
  if (param_1 == 0) {
    iVar1 = -1;
  }
  else {
    FUN_ffff0000be093c50(0xffff000010000000,3);
    iVar1 = FUN_ffff0000be093544(param_1,0xffff000018005000);
    if (iVar1 != 0) {
      FUN_ffff0000be093c50(0xffff000010000000,0);
    }
  }
  return iVar1;
}


