// ffff000090795210  size=244  FUN_ffff000090795210

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff000090795210(void)

{
  int iVar1;
  ulong uVar2;
  ulong local_40 [4];
  long local_8;
  
  local_40[2] = 0;
  local_40[3] = 0;
  local_40[0] = 0;
  local_40[1] = 0;
  local_8 = -0xffff77c00000;
  iVar1 = FUN_ffff00009077ba64
                    (DAT_ffff000090839928,s_vm_hmac_ffff0000907def11,0x1000,&local_8,0xc,0x88400000,
                     1,0);
  if ((iVar1 == 0) && (local_8 == -0xffff77c00000)) {
    _DAT_ffff00009090a580 = 0xffff000088400000;
    _DAT_ffff00009090a588 = 0xffff000088400f00;
    uVar2 = FUN_ffff000090792b30();
    if ((uVar2 & 1) == 0) {
      FUN_ffff00009077ee50(local_40,0,0x20);
      FUN_ffff00009071ebf4(0xc200040d,0x10,0,0,0,0,0,0,local_40);
      local_40[0] = local_40[0] & 0xffffffff;
    }
    else {
      local_40[0] = 0;
    }
    return local_40[0];
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0
            (s_err__d__vm_hmac__0x_p__expected___ffff0000907ea297,iVar1,local_8,0x88400000);
}


