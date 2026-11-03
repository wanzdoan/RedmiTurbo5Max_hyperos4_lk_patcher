
undefined8 FUN_ffff0000907695d0(char *param_1)

{
  char cVar1;
  
  if (DAT_ffff0000908219b0 == 1 || DAT_ffff0000908219b0 == 3) {
    cVar1 = FUN_ffff000090710bc0();
    if (cVar1 != '\x01') {
      FUN_ffff0000907869a0(2);
      FUN_ffff00009071502c(1);
      FUN_ffff00009077cea0(5000000);
      FUN_ffff000090709d18();
    }
    if (param_1 == (char *)0x0) {
      param_1 = s_UNKNOWN_ffff0000907d61b6;
    }
    FUN_ffff000090769678(param_1);
  }
  else if ((DAT_ffff0000908219b0 == 2) && (cVar1 = FUN_ffff000090710bc0(), cVar1 != '\x01')) {
    FUN_ffff0000907869a0(1);
  }
  return 0;
}


