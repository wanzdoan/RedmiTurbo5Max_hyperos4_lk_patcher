// File offset entry: 0x7e480

undefined8 FUN_0207e380(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  
  iVar1 = func_0x02091524();
  iVar2 = 0x20;
  if (iVar1 + 1 < 0x20) {
    iVar2 = iVar1 + 1;
  }
  func_0x020915ac(0x211cee8,param_1,(long)iVar2);
  if (cRam000000000211ce6c == '\x01') {
    for (lVar3 = lRam000000000210e198 + 0x78ea8; *(long *)(lVar3 + 8) != 0; lVar3 = lVar3 + 0x28) {
      if ((*(long *)(lVar3 + 0x20) != 0) && (iVar2 = func_0x02091544(param_1), iVar2 == 0)) {
        func_0x020915ac(0x211ce70,lVar3,0x28);
        func_0x020915ac(0x211ce98,*(undefined8 *)(lVar3 + 0x20),0x50);
        uRam000000000211ce90 = 0x211ce98;
        return 0x211ce70;
      }
    }
  }
  else {
    lVar3 = func_0x0205cb14(param_1);
    if (lVar3 != 0) {
      func_0x020915ac(0x211ce70,lVar3,0x28);
      return 0x211ce70;
    }
  }
  return 0;
}


