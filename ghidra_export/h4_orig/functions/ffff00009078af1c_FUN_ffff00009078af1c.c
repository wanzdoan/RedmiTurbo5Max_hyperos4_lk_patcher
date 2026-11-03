// ffff00009078af1c  size=92  FUN_ffff00009078af1c

ulong FUN_ffff00009078af1c(undefined4 param_1,undefined4 param_2)

{
  ulong local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009071ebf4(0x82000415,6,param_1,param_2,0,0,0,0,local_30);
  return local_30[0] & 0xffffffff;
}


