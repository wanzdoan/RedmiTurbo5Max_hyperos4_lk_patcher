// ffff000090769308  size=168  FUN_ffff000090769308

undefined8 FUN_ffff000090769308(char *param_1)

{
  char cVar1;
  
  if (DAT_ffff000090821990 == 1 || DAT_ffff000090821990 == 3) {
    cVar1 = FUN_ffff000090710b48();
    if (cVar1 != '\x01') {
      FUN_ffff0000907866d8(2);
      FUN_ffff000090714ee8(1);
      FUN_ffff00009077cbd8(5000000);
      FUN_ffff000090709d8c();
    }
    if (param_1 == (char *)0x0) {
      param_1 = s_UNKNOWN_ffff0000907d6120;
    }
    FUN_ffff0000907693b0(param_1);
  }
  else if ((DAT_ffff000090821990 == 2) && (cVar1 = FUN_ffff000090710b48(), cVar1 != '\x01')) {
    FUN_ffff0000907866d8(1);
  }
  return 0;
}


