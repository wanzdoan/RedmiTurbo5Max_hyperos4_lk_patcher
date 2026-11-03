
undefined8 FUN_ffff0000b803ab40(void)

{
  long lVar1;
  long lVar2;
  
  DAT_ffff0000b80f0408 = 0xffff000032800e18;
  DAT_ffff0000b80f0470 = 0xffff000032800f10;
  DAT_ffff0000b80f04d8 = &DAT_ffff000032000dd0;
  DAT_ffff0000b80f0540 = &DAT_ffff000032000e54;
  DAT_ffff0000b80f05a8 = 0xffff000032000d84;
  DAT_ffff0000b80f0610 = 0xffff000032400eac;
  DAT_ffff0000b80f0678 = 0xffff000032400f64;
  DAT_ffff0000b80f0748 = 0xffff000032400d30;
  DAT_ffff0000b80f07b0 = 0xffff000032400dcc;
  DAT_ffff0000b80f0818 = &DAT_ffff000032000e3c;
  DAT_ffff0000b80f08e8 = 0xffff000032400ea4;
  DAT_ffff0000b80f0950 = 0xffff000032400f5c;
  DAT_ffff0000b80f09b8 = 0xffff000032400d10;
  DAT_ffff0000b80f0a20 = 0xffff000032400f1c;
  DAT_ffff0000b80f0a88 = 0xffff000032400f24;
  DAT_ffff0000b80f0880 = 0xffff000032000d80;
  DAT_ffff0000b80f0bc0 = 0xffff000032c00f10;
  DAT_ffff0000b80f0c28 = 0xffff000032000df0;
  DAT_ffff0000b80f0af0 = 0xffff000032400dd4;
  DAT_ffff0000b80f0cf8 = 0xffff000032000d88;
  DAT_ffff0000b80f0d60 = 0xffff000032400eb4;
  DAT_ffff0000b80f0c90 = 0xffff000032000e58;
  DAT_ffff0000b80f0dc8 = 0xffff000032400f6c;
  DAT_ffff0000b80f06e0 = 0xffff000032400d08;
  DAT_ffff0000b80f0b58 = 0xffff000032c00e08;
  DAT_ffff0000b80f0e30 = 0xffff000032000e48;
  DAT_ffff0000b80f0e98 = 0xffff000032c00e18;
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


