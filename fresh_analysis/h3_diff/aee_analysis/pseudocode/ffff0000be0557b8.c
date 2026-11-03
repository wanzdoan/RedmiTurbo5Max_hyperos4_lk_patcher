
uint FUN_ffff0000be0557b8(undefined8 param_1,uint param_2)

{
  int iVar1;
  uint local_4;
  
  local_4 = 0xfffffffc;
  if (((param_2 & 0x80000003) == 0) &&
     (local_4 = param_2, iVar1 = FUN_ffff0000be055660(param_1,param_2,&local_4), iVar1 != 1)) {
    local_4 = 0xfffffffc;
  }
  return local_4;
}


