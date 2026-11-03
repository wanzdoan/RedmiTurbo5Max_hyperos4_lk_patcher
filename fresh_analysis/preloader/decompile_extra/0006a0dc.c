// File offset entry: 0x6a0dc

ulong FUN_02069fdc(ulong param_1,ulong param_2)

{
  ulong uVar1;
  
  uVar1 = 0;
  if (((param_1 != 0) && (0x41f < param_2)) && ((param_1 & 7) == 0)) {
    FUN_02091664(param_1,0,0x420);
    uVar1 = param_1;
  }
  return uVar1;
}


