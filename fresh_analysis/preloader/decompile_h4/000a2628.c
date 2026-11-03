// File offset entry: 0xa2628

undefined8 FUN_020a2528(long param_1,long param_2)

{
  undefined8 uVar1;
  
  uVar1 = 0x100006;
  if ((param_1 != 0) && (param_2 != 0)) {
    FUN_020a0dfc(param_1,0x1800d700,0x20);
    FUN_020a0dfc(param_2,0x1800d720,0x20);
    uVar1 = 0;
  }
  return uVar1;
}


