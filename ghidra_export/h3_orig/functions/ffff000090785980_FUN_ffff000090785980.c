// ffff000090785980  size=208  FUN_ffff000090785980

ulong FUN_ffff000090785980(int param_1,uint param_2)

{
  undefined *puVar1;
  ulong uVar2;
  undefined8 uVar3;
  long lVar4;
  undefined **ppuVar5;
  byte local_24 [4];
  
  local_24[0] = 0;
  if (param_1 == 0) {
    ppuVar5 = (undefined **)0xffff000090836660;
    puVar1 = DAT_ffff000090836668;
    if (DAT_ffff000090836668 != (undefined *)0x0) goto LAB_ffff0000907859dc;
    uVar3 = 5;
  }
  else {
    ppuVar5 = &PTR_PTR_ffff000090836458;
    puVar1 = DAT_ffff000090836460;
    if (DAT_ffff000090836460 != (undefined *)0x0) goto LAB_ffff0000907859dc;
    uVar3 = 4;
  }
  puVar1 = (undefined *)FUN_ffff000090740378(1,uVar3);
  ppuVar5[1] = puVar1;
LAB_ffff0000907859dc:
  if ((param_2 < *(uint *)(ppuVar5 + 2)) || (*(uint *)((long)ppuVar5 + 0x14) < param_2)) {
    uVar2 = 0xfffffffe;
  }
  else {
    lVar4 = *(long *)(*ppuVar5 + 0x28);
    if ((lVar4 != 0) &&
       (uVar2 = (ulong)(param_2 - *(uint *)(ppuVar5 + 2)), *(char *)(lVar4 + uVar2) != -1)) {
      uVar2 = FUN_ffff000090740264
                        (puVar1,*(undefined2 *)(*(long *)(*ppuVar5 + 0x20) + uVar2 * 2),local_24,1);
      if ((int)uVar2 < 0) {
        return uVar2;
      }
      return (ulong)local_24[0];
    }
    uVar2 = 0xffffffff;
  }
  return uVar2;
}


