// ffff000090795044  size=132  FUN_ffff000090795044

ulong FUN_ffff000090795044(void)

{
  ulong uVar1;
  ulong local_40 [4];
  
  uVar1 = FUN_ffff000090792870();
  if ((uVar1 & 1) == 0) {
    local_40[2] = 0;
    local_40[3] = 0;
    local_40[0] = 0;
    local_40[1] = 0;
    FUN_ffff00009077eb88(local_40,0,0x20);
    FUN_ffff00009071ea6c(0xc200040d,0x10,1,0,0,0,0,0,local_40);
    local_40[0] = local_40[0] & 0xffffffff;
  }
  else {
    local_40[0] = 0;
  }
  return local_40[0];
}


