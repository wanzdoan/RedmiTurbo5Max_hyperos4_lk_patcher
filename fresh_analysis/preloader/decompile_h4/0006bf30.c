// File offset entry: 0x6bf30

long FUN_0206be30(void)

{
  long lVar1;
  long *plVar2;
  
  lVar1 = 0;
  plVar2 = (long *)(*plRam0000000002103338 + 0x7d700);
  while( true ) {
    if (lVar1 == 0x80) {
      return 0xffffffff;
    }
    if (*plVar2 == 0) break;
    lVar1 = lVar1 + 1;
    plVar2 = plVar2 + 0x13;
  }
  return lVar1;
}


