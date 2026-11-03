// File offset entry: 0x63718

/* WARNING: Possible PIC construction at 0x0206379c: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020638f8: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020637a0) */
/* WARNING: Removing unreachable block (ram,0x020637b8) */
/* WARNING: Removing unreachable block (ram,0x0206380c) */
/* WARNING: Removing unreachable block (ram,0x02063820) */
/* WARNING: Removing unreachable block (ram,0x020638f4) */
/* WARNING: Removing unreachable block (ram,0x02063830) */
/* WARNING: Removing unreachable block (ram,0x02063870) */
/* WARNING: Removing unreachable block (ram,0x0206387c) */
/* WARNING: Removing unreachable block (ram,0x02063834) */
/* WARNING: Removing unreachable block (ram,0x02063884) */
/* WARNING: Removing unreachable block (ram,0x020638c8) */
/* WARNING: Removing unreachable block (ram,0x02063890) */
/* WARNING: Removing unreachable block (ram,0x02063898) */
/* WARNING: Removing unreachable block (ram,0x0206383c) */
/* WARNING: Removing unreachable block (ram,0x02063844) */
/* WARNING: Removing unreachable block (ram,0x02063864) */
/* WARNING: Removing unreachable block (ram,0x020638d0) */
/* WARNING: Removing unreachable block (ram,0x020638d4) */
/* WARNING: Removing unreachable block (ram,0x02063800) */
/* WARNING: Removing unreachable block (ram,0x020638f8) */
/* WARNING: Removing unreachable block (ram,0x020637a4) */
/* WARNING: Removing unreachable block (ram,0x020638fc) */
/* WARNING: Removing unreachable block (ram,0x02063918) */
/* WARNING: Removing unreachable block (ram,0x02063900) */

undefined8 FUN_02063618(undefined4 param_1,undefined4 param_2)

{
  char cVar1;
  undefined1 in_ZR;
  undefined1 in_CY;
  long extraout_x8;
  int iVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  long lVar6;
  undefined4 uStack_2c;
  undefined8 uStack_28;
  
  uStack_28 = *puRam0000000002103280;
  uStack_2c = param_1;
  func_0x0206333c(&uStack_2c);
  func_0x0206393c();
  if ((bool)in_CY && !(bool)in_ZR) {
LAB_0206379c:
    return 0xfffffffe;
  }
  cVar1 = *(char *)(extraout_x8 + 0x20e69e4);
  if ((cVar1 == -1) && (*(char *)(extraout_x8 + 0x20e747c) == -1)) {
    return 0xffffffff;
  }
  switch(param_2) {
  case 0:
    iVar2 = 8;
    if (cVar1 != -1) {
      lVar4 = 0x20e6af4;
      lVar5 = 0x20e6f30;
      lVar6 = 0x20e65a8;
      iVar3 = 8;
      goto code_r0x0206376c;
    }
    iVar3 = 8;
    iVar2 = 8;
    break;
  case 1:
    if (cVar1 != -1) {
      return 0xffffffff;
    }
    goto code_r0x020636f8;
  case 2:
    if (*(char *)(extraout_x8 + 0x20e747c) == -1) {
      return 0xffffffff;
    }
code_r0x020636f8:
    iVar3 = 8;
    iVar2 = 4;
    break;
  case 3:
    if (*(char *)(extraout_x8 + 0x20e747c) == -1) {
      return 0xffffffff;
    }
    iVar3 = 4;
    iVar2 = 8;
    break;
  case 4:
    if (*(char *)(extraout_x8 + 0x20e747c) == -1) {
      return 0xffffffff;
    }
    iVar3 = 4;
    iVar2 = 4;
    break;
  default:
    goto LAB_0206379c;
  }
  lVar4 = 0x20e7ad8;
  cVar1 = *(char *)(extraout_x8 + 0x20e79c8);
  lVar5 = 0x20e7f14;
  lVar6 = 0x20e758c;
code_r0x0206376c:
  *(int *)(ulong)(uint)(*(int *)(lVar6 + extraout_x8 * 4) + iVar2) =
       (int)(1L << ((long)cVar1 & 0x3fU));
  *(int *)(ulong)(uint)(*(int *)(lVar4 + extraout_x8 * 4) + iVar3) =
       (int)(1L << ((long)*(char *)(lVar5 + extraout_x8) & 0x3fU));
  return 0;
}


