
ulong FUN_ffff0000be072a08(void)

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
  iVar1 = FUN_ffff0000be05d9a8
                    (PTR_DAT_ffff0000be0f5570,s_vm_hmac_ffff0000be0ae06e,0x1000,&local_8,0xc,
                     0x88400000,1,0);
  if ((iVar1 == 0) && (local_8 == -0xffff77c00000)) {
    DAT_ffff0000be1b4c00 = 0xffff000088400000;
    DAT_ffff0000be1b4c08 = 0xffff000088400f00;
    uVar2 = FUN_ffff0000be0705d0();
    if ((uVar2 & 1) == 0) {
      FUN_ffff0000be060ae0(local_40,0,0x20);
      FUN_ffff0000be01d644(0xc200040d,0x10,0,0,0,0,0,0,local_40);
      local_40[0] = local_40[0] & 0xffffffff;
    }
    else {
      local_40[0] = 0;
    }
    return local_40[0];
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee24
            (s_err__d__vm_hmac__0x_p__expected___ffff0000be0b5d3c,iVar1,local_8,0x88400000);
}


