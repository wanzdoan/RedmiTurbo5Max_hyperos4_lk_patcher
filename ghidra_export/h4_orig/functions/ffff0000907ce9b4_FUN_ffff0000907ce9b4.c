// ffff0000907ce9b4  size=96  FUN_ffff0000907ce9b4

undefined8 FUN_ffff0000907ce9b4(long param_1,byte param_2)

{
  undefined8 uVar1;
  
  uVar1 = 0xffffffff;
  if ((param_1 != 0) && (0x1f < param_2)) {
    FUN_ffff00009077ee50(param_1,0,0x20);
    FUN_ffff00009077f6fc(param_1,0x20,&DAT_ffff0000907d7339);
    uVar1 = 0;
  }
  return uVar1;
}


