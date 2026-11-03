// ffff000090758c90  size=352  FUN_ffff000090758c90

undefined8 FUN_ffff000090758c90(void)

{
  long lVar1;
  long lVar2;
  
  DAT_ffff000090820b98 = 0xffff000032800e18;
  DAT_ffff000090820c00 = 0xffff000032800f10;
  DAT_ffff000090820c68 = &DAT_ffff000032000dd0;
  DAT_ffff000090820cd0 = &DAT_ffff000032000e54;
  DAT_ffff000090820d38 = 0xffff000032000d84;
  DAT_ffff000090820da0 = 0xffff000032400eac;
  DAT_ffff000090820e08 = 0xffff000032400f64;
  DAT_ffff000090820ed8 = 0xffff000032400d30;
  DAT_ffff000090820f40 = 0xffff000032400dcc;
  DAT_ffff000090820fa8 = &DAT_ffff000032000e3c;
  DAT_ffff000090821078 = 0xffff000032400ea4;
  DAT_ffff0000908210e0 = 0xffff000032400f5c;
  DAT_ffff000090821148 = 0xffff000032400d10;
  DAT_ffff0000908211b0 = 0xffff000032400f1c;
  DAT_ffff000090821218 = 0xffff000032400f24;
  DAT_ffff000090821010 = 0xffff000032000d80;
  DAT_ffff000090821350 = 0xffff000032c00f10;
  DAT_ffff0000908213b8 = 0xffff000032000df0;
  DAT_ffff000090821280 = 0xffff000032400dd4;
  DAT_ffff000090821488 = 0xffff000032000d88;
  DAT_ffff0000908214f0 = 0xffff000032400eb4;
  DAT_ffff000090821420 = 0xffff000032000e58;
  DAT_ffff000090821558 = 0xffff000032400f6c;
  DAT_ffff000090820e70 = 0xffff000032400d08;
  DAT_ffff0000908212e8 = 0xffff000032c00e08;
  DAT_ffff0000908215c0 = 0xffff000032000e48;
  DAT_ffff000090821628 = 0xffff000032c00e18;
  lVar2 = 0;
  do {
    lVar1 = lVar2 + 4;
    *(uint *)(&DAT_ffff000032a60380 + lVar2) = *(uint *)(&DAT_ffff000032a60380 + lVar2) & 0xfffffffe
    ;
    *(uint *)(&DAT_ffff000032a70380 + lVar2) = *(uint *)(&DAT_ffff000032a70380 + lVar2) & 0xfffffffe
    ;
    lVar2 = lVar1;
  } while (lVar1 != 0x40);
  return 0;
}


