
ulong FUN_ffff000090785c48(int param_1,uint param_2)

{
  undefined *puVar1;
  ulong uVar2;
  undefined8 uVar3;
  long lVar4;
  undefined **ppuVar5;
  byte local_24 [4];
  
  local_24[0] = 0;
  if (param_1 == 0) {
    ppuVar5 = (undefined **)0xffff000090836680;
    puVar1 = DAT_ffff000090836688;
    if (DAT_ffff000090836688 != (undefined *)0x0) goto LAB_ffff000090785ca4;
    uVar3 = 5;
  }
  else {
    ppuVar5 = &PTR_PTR_ffff000090836478;
    puVar1 = DAT_ffff000090836480;
    if (DAT_ffff000090836480 != (undefined *)0x0) goto LAB_ffff000090785ca4;
    uVar3 = 4;
  }
  puVar1 = (undefined *)FUN_ffff000090740610(1,uVar3);
  ppuVar5[1] = puVar1;
LAB_ffff000090785ca4:
  if ((param_2 < *(uint *)(ppuVar5 + 2)) || (*(uint *)((long)ppuVar5 + 0x14) < param_2)) {
    uVar2 = 0xfffffffe;
  }
  else {
    lVar4 = *(long *)(*ppuVar5 + 0x28);
    if ((lVar4 != 0) &&
       (uVar2 = (ulong)(param_2 - *(uint *)(ppuVar5 + 2)), *(char *)(lVar4 + uVar2) != -1)) {
      uVar2 = FUN_ffff0000907404fc
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


