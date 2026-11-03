
undefined8 FUN_ffff0000be045390(ulong param_1,uint param_2)

{
  long *plVar1;
  long lVar2;
  ulong uVar3;
  
  if (0x101 < param_2) {
    return 0;
  }
  if (*(uint *)(&DAT_ffff0000be0cb73c + (ulong)param_2 * 0xc) == 0xffffffff) {
    return 0;
  }
  switch(*(int *)(&DAT_ffff0000be0cb740 + (ulong)param_2 * 0xc)) {
  case 0:
    plVar1 = &DAT_ffff0000be0dae08;
    break;
  case 1:
    plVar1 = &DAT_ffff0000be0dae08;
    goto LAB_ffff0000be045450;
  case 2:
    plVar1 = &DAT_ffff0000be0dae18;
    break;
  default:
    if ((param_2 & 0xfffffffe) == 0xc2) {
      plVar1 = &DAT_ffff0000be0dae18;
    }
    else {
      if (*(int *)(&DAT_ffff0000be0cb740 + (ulong)param_2 * 0xc) == 0x16) {
        plVar1 = &DAT_ffff0000be0dae20;
        break;
      }
      if ((param_2 != 0xc4) && (param_2 != 0xc1)) {
        return 0;
      }
      plVar1 = &DAT_ffff0000be0dae20;
    }
    goto LAB_ffff0000be045450;
  case 0x14:
    plVar1 = &DAT_ffff0000be0dae10;
    break;
  case 0x15:
    plVar1 = &DAT_ffff0000be0dae10;
LAB_ffff0000be045450:
    lVar2 = *plVar1 + 0x38;
    goto code_r0xffff0000be045478;
  }
  lVar2 = *plVar1 + 0x34;
code_r0xffff0000be045478:
  uVar3 = -(param_1 >> 0x1f & 1) & 0xffffffe000000000 | (param_1 & 0xffffffff) << 5;
  *(uint *)(lVar2 + uVar3) =
       *(uint *)(lVar2 + uVar3) |
       1 << (ulong)(*(uint *)(&DAT_ffff0000be0cb73c + (ulong)param_2 * 0xc) & 0x1f);
  return 0;
}


