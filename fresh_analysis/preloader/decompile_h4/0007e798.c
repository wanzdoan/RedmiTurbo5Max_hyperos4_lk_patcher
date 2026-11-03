// File offset entry: 0x7e798

undefined8 FUN_0207e698(undefined8 param_1)

{
  int iVar1;
  long lVar2;
  
  if (cRam000000000211ce6c == '\x01') {
    for (lVar2 = lRam000000000210e198 + 0x78ea8; *(long *)(lVar2 + 8) != 0; lVar2 = lVar2 + 0x28) {
      if ((*(long *)(lVar2 + 0x20) != 0) && (iVar1 = func_0x02091544(param_1), iVar1 == 0)) {
        func_0x020915ac(0x211ce70,lVar2,0x28);
        func_0x020915ac(0x211ce98,*(undefined8 *)(lVar2 + 0x20),0x50);
        uRam000000000211ce90 = 0x211ce98;
        return 0x211ce70;
      }
    }
  }
  else {
    lVar2 = func_0x0205cb14(param_1);
    if (lVar2 != 0) {
      func_0x020915ac(0x211ce70,lVar2,0x28);
      return 0x211ce70;
    }
  }
  return 0;
}


