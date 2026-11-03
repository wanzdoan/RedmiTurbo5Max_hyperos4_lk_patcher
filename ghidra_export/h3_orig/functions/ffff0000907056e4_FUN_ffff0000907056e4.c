// ffff0000907056e4  size=96  FUN_ffff0000907056e4

undefined8 FUN_ffff0000907056e4(undefined8 param_1)

{
  long lVar1;
  undefined8 uVar2;
  uint local_4;
  
  local_4 = 0;
  lVar1 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea425);
  if (lVar1 == 0) {
    uVar2 = 0xfffffff9;
  }
  else {
    uVar2 = FUN_ffff0000907372bc(lVar1,0x107,&local_4);
    if ((int)uVar2 == 0) {
      *(bool *)param_1 = (local_4 & 0x80) == 0;
    }
  }
  return uVar2;
}


