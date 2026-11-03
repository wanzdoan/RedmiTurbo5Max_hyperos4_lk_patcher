
undefined8 FUN_ffff00009075a3b4(ulong param_1,uint param_2)

{
  long *plVar1;
  long lVar2;
  ulong uVar3;
  
  if (0x101 < param_2) {
    return 0;
  }
  if (*(uint *)(&DAT_ffff00009080d804 + (ulong)param_2 * 0xc) == 0xffffffff) {
    return 0;
  }
  switch(*(int *)(&DAT_ffff00009080d808 + (ulong)param_2 * 0xc)) {
  case 0:
    plVar1 = &DAT_ffff00009081e4e8;
    break;
  case 1:
    plVar1 = &DAT_ffff00009081e4e8;
    goto LAB_ffff00009075a474;
  case 2:
    plVar1 = &DAT_ffff00009081e4f8;
    break;
  default:
    if ((param_2 & 0xfffffffe) == 0xc2) {
      plVar1 = &DAT_ffff00009081e4f8;
    }
    else {
      if (*(int *)(&DAT_ffff00009080d808 + (ulong)param_2 * 0xc) == 0x16) {
        plVar1 = &DAT_ffff00009081e500;
        break;
      }
      if ((param_2 != 0xc4) && (param_2 != 0xc1)) {
        return 0;
      }
      plVar1 = &DAT_ffff00009081e500;
    }
    goto LAB_ffff00009075a474;
  case 0x14:
    plVar1 = &DAT_ffff00009081e4f0;
    break;
  case 0x15:
    plVar1 = &DAT_ffff00009081e4f0;
LAB_ffff00009075a474:
    lVar2 = *plVar1 + 0x38;
    goto code_r0xffff00009075a49c;
  }
  lVar2 = *plVar1 + 0x34;
code_r0xffff00009075a49c:
  uVar3 = -(param_1 >> 0x1f & 1) & 0xffffffe000000000 | (param_1 & 0xffffffff) << 5;
  *(uint *)(lVar2 + uVar3) =
       *(uint *)(lVar2 + uVar3) |
       1 << (ulong)(*(uint *)(&DAT_ffff00009080d804 + (ulong)param_2 * 0xc) & 0x1f);
  return 0;
}


