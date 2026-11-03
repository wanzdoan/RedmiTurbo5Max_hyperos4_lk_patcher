// File offset entry: 0x638bc

/* WARNING: Possible PIC construction at 0x020638f8: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020638fc) */
/* WARNING: Removing unreachable block (ram,0x02063918) */
/* WARNING: Removing unreachable block (ram,0x02063900) */

undefined8 FUN_020637bc(uint param_1,int param_2)

{
  char cVar1;
  int iVar2;
  long lVar3;
  int iVar4;
  long lVar5;
  ulong uVar6;
  uint uStack_3c;
  undefined8 uStack_38;
  
  uStack_38 = *puRam0000000002103280;
  uStack_3c = param_1;
  func_0x0206333c(&uStack_3c);
  uVar6 = (ulong)uStack_3c;
  if (0x10e < uStack_3c) {
    return 0xfffffffe;
  }
  cVar1 = *(char *)(uVar6 + 0x20e69e4);
  if (((long)cVar1 == 0xffffffffffffffff) && (*(char *)(uVar6 + 0x20e747c) == -1)) {
    return 0xffffffff;
  }
  if (param_2 == 0) {
    iVar2 = 4;
    if (cVar1 != -1) {
      iVar4 = 8;
code_r0x02063898:
      lVar3 = 0x20e6af4;
      *(int *)(ulong)(uint)(*(int *)(uVar6 * 4 + 0x20e65a8) + iVar4) =
           (int)(1L << ((long)cVar1 & 0x3fU));
      lVar5 = 0x20e6f30;
      goto LAB_020638d4;
    }
  }
  else if (param_2 == 1) {
    iVar2 = 8;
    if (cVar1 != -1) {
      iVar4 = 4;
      goto code_r0x02063898;
    }
  }
  else {
    if (param_2 != 2) {
      return 0xfffffffe;
    }
    FUN_02063618(uVar6,0);
    if (*(char *)(uVar6 + 0x20e69e4) != -1) {
      return 0;
    }
    iVar2 = 4;
  }
  lVar3 = 0x20e7040;
  lVar5 = 0x20e747c;
LAB_020638d4:
  *(int *)(ulong)(uint)(*(int *)(lVar3 + uVar6 * 4) + iVar2) =
       (int)(1L << ((long)*(char *)(lVar5 + uVar6) & 0x3fU));
  return 0;
}


