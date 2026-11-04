
undefined8 FUN_ffff000090769e00(char *param_1)

{
  char cVar1;

  if (DAT_ffff000090819968 == 1 || DAT_ffff000090819968 == 3) {
    cVar1 = FUN_ffff000090710708();
    if (cVar1 != '\x01') {
      FUN_ffff0000907871ec(2);
      FUN_ffff000090714890(1);
      FUN_ffff00009077d808(5000000);
      FUN_ffff000090709e28();
    }
    if (param_1 == (char *)0x0) {
      param_1 = s_UNKNOWN_ffff0000907ce430;
    }
    FUN_ffff000090769ea8(param_1);
  }
  else if ((DAT_ffff000090819968 == 2) && (cVar1 = FUN_ffff000090710708(), cVar1 != '\x01')) {
    FUN_ffff0000907871ec(1);
  }
  return 0;
}
