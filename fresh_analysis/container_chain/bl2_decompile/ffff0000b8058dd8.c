
ulong FUN_ffff0000b8058dd8(void)

{
  ulong uVar1;
  ulong local_40 [4];
  
  uVar1 = FUN_ffff0000b8056844();
  if ((uVar1 & 1) == 0) {
    local_40[2] = 0;
    local_40[3] = 0;
    local_40[0] = 0;
    local_40[1] = 0;
    FUN_ffff0000b804d070(local_40,0,0x20);
    FUN_ffff0000b800e6ac(0xc200040d,0x10,1,0,0,0,0,0,local_40);
    local_40[0] = local_40[0] & 0xffffffff;
  }
  else {
    local_40[0] = 0;
  }
  return local_40[0];
}


