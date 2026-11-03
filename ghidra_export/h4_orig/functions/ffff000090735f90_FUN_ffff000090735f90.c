// ffff000090735f90  size=112  FUN_ffff000090735f90

void FUN_ffff000090735f90(long param_1)

{
  long lVar1;
  undefined2 *puVar2;
  long lVar3;
  
  lVar3 = 0;
  do {
    puVar2 = (undefined2 *)(param_1 + 0xc + lVar3);
    lVar1 = lVar3 + 0x10;
    *(undefined2 *)(lVar3 + -0xffff6f792534) = puVar2[-2];
    *(undefined2 *)(lVar3 + -0xffff6f792532) = puVar2[-1];
    *(undefined2 *)(lVar3 + -0xffff6f792530) = *puVar2;
    *(undefined2 *)(lVar3 + -0xffff6f79252e) = puVar2[1];
    *(undefined2 *)(lVar3 + -0xffff6f79252c) = puVar2[2];
    *(undefined2 *)(lVar3 + -0xffff6f79252a) = puVar2[3];
    *(undefined2 *)(lVar3 + -0xffff6f792528) = puVar2[4];
    *(undefined2 *)(lVar3 + -0xffff6f792526) = puVar2[5];
    lVar3 = lVar1;
  } while (lVar1 != 0x100);
  return;
}


