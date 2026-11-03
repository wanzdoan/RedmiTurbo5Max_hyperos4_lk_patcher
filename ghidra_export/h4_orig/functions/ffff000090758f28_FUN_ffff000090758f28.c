// ffff000090758f28  size=352  FUN_ffff000090758f28

undefined8 FUN_ffff000090758f28(void)

{
  long lVar1;
  long lVar2;
  
  DAT_ffff000090820bb8 = 0xffff000032800e18;
  DAT_ffff000090820c20 = 0xffff000032800f10;
  DAT_ffff000090820c88 = &DAT_ffff000032000dd0;
  DAT_ffff000090820cf0 = &DAT_ffff000032000e54;
  DAT_ffff000090820d58 = 0xffff000032000d84;
  DAT_ffff000090820dc0 = 0xffff000032400eac;
  DAT_ffff000090820e28 = 0xffff000032400f64;
  DAT_ffff000090820ef8 = 0xffff000032400d30;
  DAT_ffff000090820f60 = 0xffff000032400dcc;
  DAT_ffff000090820fc8 = &DAT_ffff000032000e3c;
  DAT_ffff000090821098 = 0xffff000032400ea4;
  DAT_ffff000090821100 = 0xffff000032400f5c;
  DAT_ffff000090821168 = 0xffff000032400d10;
  DAT_ffff0000908211d0 = 0xffff000032400f1c;
  DAT_ffff000090821238 = 0xffff000032400f24;
  DAT_ffff000090821030 = 0xffff000032000d80;
  DAT_ffff000090821370 = 0xffff000032c00f10;
  DAT_ffff0000908213d8 = 0xffff000032000df0;
  DAT_ffff0000908212a0 = 0xffff000032400dd4;
  DAT_ffff0000908214a8 = 0xffff000032000d88;
  DAT_ffff000090821510 = 0xffff000032400eb4;
  DAT_ffff000090821440 = 0xffff000032000e58;
  DAT_ffff000090821578 = 0xffff000032400f6c;
  DAT_ffff000090820e90 = 0xffff000032400d08;
  DAT_ffff000090821308 = 0xffff000032c00e08;
  DAT_ffff0000908215e0 = 0xffff000032000e48;
  DAT_ffff000090821648 = 0xffff000032c00e18;
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


