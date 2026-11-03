
undefined1 * FUN_ffff0000be05cb80(void)

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
  FUN_ffff0000be01a730(&LAB_ffff0000be0ef258,0xffffffff);
  if ((undefined **)PTR_LOOP_ffff0000be0ef250 == &PTR_LOOP_ffff0000be0ef248) {
code_r0xffff0000be05cc60:
    FUN_ffff0000be01a7c8(&LAB_ffff0000be0ef258);
  }
  else {
    bVar2 = false;
    ppuVar3 = (undefined **)PTR_LOOP_ffff0000be0ef250;
    do {
      if (!bVar2) {
        puVar5 = ppuVar3[6];
        if (puVar5 == (undefined *)0x0) {
          bVar2 = false;
        }
        else {
          ppuVar4 = (undefined **)ppuVar3[9];
          if (ppuVar4 == ppuVar3 + 8) goto code_r0xffff0000be05cc60;
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
    } while (ppuVar3 != &PTR_LOOP_ffff0000be0ef248);
    FUN_ffff0000be01a7c8(&LAB_ffff0000be0ef258);
    if (bVar2) {
      if ((undefined ***)local_28 == &local_30) {
        return (undefined1 *)0x0;
      }
      return local_28;
    }
  }
  return (undefined1 *)0x0;
}


