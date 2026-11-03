
undefined8 FUN_ffff0000be043f04(void)

{
  long lVar1;
  long lVar2;
  
  DAT_ffff0000be0dd4d8 = 0xffff000032800e18;
  DAT_ffff0000be0dd540 = 0xffff000032800f10;
  DAT_ffff0000be0dd5a8 = &DAT_ffff000032000dd0;
  DAT_ffff0000be0dd610 = &DAT_ffff000032000e54;
  DAT_ffff0000be0dd678 = 0xffff000032000d84;
  DAT_ffff0000be0dd6e0 = 0xffff000032400eac;
  DAT_ffff0000be0dd748 = 0xffff000032400f64;
  DAT_ffff0000be0dd818 = 0xffff000032400d30;
  DAT_ffff0000be0dd880 = 0xffff000032400dcc;
  DAT_ffff0000be0dd8e8 = &DAT_ffff000032000e3c;
  DAT_ffff0000be0dd9b8 = 0xffff000032400ea4;
  DAT_ffff0000be0dda20 = 0xffff000032400f5c;
  DAT_ffff0000be0dda88 = 0xffff000032400d10;
  DAT_ffff0000be0ddaf0 = 0xffff000032400f1c;
  DAT_ffff0000be0ddb58 = 0xffff000032400f24;
  DAT_ffff0000be0dd950 = 0xffff000032000d80;
  DAT_ffff0000be0ddc90 = 0xffff000032c00f10;
  DAT_ffff0000be0ddcf8 = 0xffff000032000df0;
  DAT_ffff0000be0ddbc0 = 0xffff000032400dd4;
  DAT_ffff0000be0dddc8 = 0xffff000032000d88;
  DAT_ffff0000be0dde30 = 0xffff000032400eb4;
  DAT_ffff0000be0ddd60 = 0xffff000032000e58;
  DAT_ffff0000be0dde98 = 0xffff000032400f6c;
  DAT_ffff0000be0dd7b0 = 0xffff000032400d08;
  DAT_ffff0000be0ddc28 = 0xffff000032c00e08;
  DAT_ffff0000be0ddf00 = 0xffff000032000e48;
  DAT_ffff0000be0ddf68 = 0xffff000032c00e18;
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


