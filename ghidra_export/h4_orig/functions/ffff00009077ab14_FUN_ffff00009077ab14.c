// ffff00009077ab14  size=104  FUN_ffff00009077ab14

undefined * FUN_ffff00009077ab14(undefined *param_1)

{
  undefined **ppuVar1;
  undefined *puVar2;
  
  ppuVar1 = (undefined **)PTR_LOOP_ffff000090832858;
  while( true ) {
    if (ppuVar1 == &PTR_LOOP_ffff000090832850) {
      return (undefined *)0x0;
    }
    puVar2 = ppuVar1[4];
    if ((puVar2 <= param_1) && (param_1 <= puVar2 + (long)ppuVar1[5] + -1)) break;
    ppuVar1 = (undefined **)ppuVar1[1];
  }
  return ppuVar1[7] + ((ulong)((long)param_1 - (long)puVar2) >> 0xc) * 0x18;
}


