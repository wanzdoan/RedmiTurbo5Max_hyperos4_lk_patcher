// ffff000090735cf8  size=112  FUN_ffff000090735cf8

void FUN_ffff000090735cf8(long param_1)

{
  long lVar1;
  undefined2 *puVar2;
  long lVar3;
  
  lVar3 = 0;
  do {
    puVar2 = (undefined2 *)(param_1 + 0xc + lVar3);
    lVar1 = lVar3 + 0x10;
    *(undefined2 *)(lVar3 + -0xffff6f7925cc) = puVar2[-2];
    *(undefined2 *)(lVar3 + -0xffff6f7925ca) = puVar2[-1];
    *(undefined2 *)(lVar3 + -0xffff6f7925c8) = *puVar2;
    *(undefined2 *)(lVar3 + -0xffff6f7925c6) = puVar2[1];
    *(undefined2 *)(lVar3 + -0xffff6f7925c4) = puVar2[2];
    *(undefined2 *)(lVar3 + -0xffff6f7925c2) = puVar2[3];
    *(undefined2 *)(lVar3 + -0xffff6f7925c0) = puVar2[4];
    *(undefined2 *)(lVar3 + -0xffff6f7925be) = puVar2[5];
    lVar3 = lVar1;
  } while (lVar1 != 0x100);
  return;
}


