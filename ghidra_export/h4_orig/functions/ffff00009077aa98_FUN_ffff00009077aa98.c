// ffff00009077aa98  size=124  FUN_ffff00009077aa98

undefined * FUN_ffff00009077aa98(undefined *param_1)

{
  undefined **ppuVar1;
  undefined **ppuVar2;
  undefined *puVar3;
  
  ppuVar2 = (undefined **)PTR_LOOP_ffff000090832858;
  if ((undefined **)PTR_LOOP_ffff000090832858 != &PTR_LOOP_ffff000090832850) {
    do {
      puVar3 = ppuVar2[7];
      if ((puVar3 <= param_1) && (param_1 < puVar3 + ((ulong)ppuVar2[5] >> 0xc) * 0x18)) {
        return ppuVar2[4] + ((ulong)((long)param_1 - (long)puVar3) / 0x18) * 0x1000;
      }
      ppuVar1 = ppuVar2 + 1;
      ppuVar2 = (undefined **)*ppuVar1;
    } while ((undefined **)*ppuVar1 != &PTR_LOOP_ffff000090832850);
  }
  return (undefined *)0xffffffffffffffff;
}


