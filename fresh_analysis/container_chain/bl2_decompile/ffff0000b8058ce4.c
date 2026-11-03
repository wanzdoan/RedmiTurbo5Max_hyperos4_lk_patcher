
ulong FUN_ffff0000b8058ce4(void)

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
  iVar1 = FUN_ffff0000b804a5f8
                    (PTR_DAT_ffff0000b81086d8,s_vm_hmac_ffff0000b808d862,0x1000,&local_8,0xc,
                     0x88400000,1,0);
  if ((iVar1 == 0) && (local_8 == -0xffff77c00000)) {
    DAT_ffff0000b81b6940 = 0xffff000088400000;
    DAT_ffff0000b81b6948 = 0xffff000088400f00;
    uVar2 = FUN_ffff0000b8056844();
    if ((uVar2 & 1) == 0) {
      FUN_ffff0000b804d070(local_40,0,0x20);
      FUN_ffff0000b800e6ac(0xc200040d,0x10,0,0,0,0,0,0,local_40);
      local_40[0] = local_40[0] & 0xffffffff;
    }
    else {
      local_40[0] = 0;
    }
    return local_40[0];
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b744
            (s_err__d__vm_hmac__0x_p__expected___ffff0000b8093df8,iVar1,local_8,0x88400000);
}


