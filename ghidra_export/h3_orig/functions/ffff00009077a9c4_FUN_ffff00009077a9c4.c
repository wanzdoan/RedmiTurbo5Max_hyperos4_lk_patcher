// ffff00009077a9c4  size=256  FUN_ffff00009077a9c4

undefined1 * FUN_ffff00009077a9c4(void)

{
  undefined8 *puVar1;
  bool bVar2;
  undefined **ppuVar3;
  undefined **ppuVar4;
  undefined *puVar5;
  undefined **local_30;
  undefined1 *local_28;
  
  local_28 = (undefined1 *)&local_30;
  local_30 = (undefined **)&local_30;
  FUN_ffff0000907175f4(&LAB_ffff000090832840,0xffffffff);
  if ((undefined **)PTR_LOOP_ffff000090832838 == &PTR_LOOP_ffff000090832830) {
code_r0xffff00009077aaa4:
    FUN_ffff00009071768c(&LAB_ffff000090832840);
  }
  else {
    bVar2 = false;
    ppuVar3 = (undefined **)PTR_LOOP_ffff000090832838;
    do {
      if (!bVar2) {
        puVar5 = ppuVar3[6];
        if (puVar5 == (undefined *)0x0) {
          bVar2 = false;
        }
        else {
          ppuVar4 = (undefined **)ppuVar3[9];
          if (ppuVar4 == ppuVar3 + 8) goto code_r0xffff00009077aaa4;
          puVar1 = (undefined8 *)ppuVar4[1];
          bVar2 = true;
          *puVar1 = *ppuVar4;
          *(undefined8 **)(*ppuVar4 + 8) = puVar1;
          *ppuVar4 = (undefined *)0x0;
          ppuVar3[6] = puVar5 + -1;
          *ppuVar4 = (undefined *)local_30;
          ppuVar4[1] = (undefined *)&local_30;
          *(uint *)(ppuVar4 + 2) = *(uint *)(ppuVar4 + 2) | 1;
          local_30[1] = (undefined *)ppuVar4;
          local_30 = ppuVar4;
        }
      }
      ppuVar3 = (undefined **)ppuVar3[1];
    } while (ppuVar3 != &PTR_LOOP_ffff000090832830);
    FUN_ffff00009071768c(&LAB_ffff000090832840);
    if (bVar2) {
      if ((undefined ***)local_28 == &local_30) {
        return (undefined1 *)0x0;
      }
      return local_28;
    }
  }
  return (undefined1 *)0x0;
}


