
undefined8 FUN_ffff0000b8017200(undefined8 param_1)

{
  int iVar1;
  long lVar2;
  int local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff0000b8047d28(param_1,s__trusty_ffff0000b80932a9);
  if (((-1 < iVar1) &&
      (lVar2 = FUN_ffff0000b8047fb0(param_1,iVar1,s_status_ffff0000b8096206,&local_4), lVar2 != 0))
     && (0 < local_4)) {
    iVar1 = FUN_ffff0000b804d248(lVar2 + 0xc,&DAT_ffff0000b808dcff);
    if ((iVar1 != 0) &&
       (iVar1 = FUN_ffff0000b804d248(lVar2 + 0xc,&DAT_ffff0000b8087087), iVar1 != 0)) {
      DAT_ffff0000b811b0e0 = 1;
    }
  }
  return 0;
}


