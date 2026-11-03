// File offset entry: 0x7e6d4

undefined8 FUN_0207e5d4(void)

{
  long *plVar1;
  int iVar2;
  long lVar3;
  
  plVar1 = plRam0000000002103338;
  lVar3 = *plRam0000000002103338;
  *(undefined4 *)(lVar3 + 0x766a0) = 0;
  func_0x02091664(lVar3 + 0x766a4,0,0x2800);
  func_0x02091664(*plVar1 + 0x78ea8,0,0x1400);
  func_0x02091664(*plVar1 + 0x7a2a8,0,0x2800);
  lVar3 = 0;
  uRam000000000211ce6c = 1;
  do {
    if (lVar3 == 8) {
      return 0;
    }
    iVar2 = (**(code **)(lVar3 + 0x20ec198))(*plVar1 + 0x78ea8);
    lVar3 = lVar3 + 8;
  } while (iVar2 != 0);
  return 0;
}


