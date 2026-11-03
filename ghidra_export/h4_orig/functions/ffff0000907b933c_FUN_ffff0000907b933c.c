// ffff0000907b933c  size=460  FUN_ffff0000907b933c

undefined4 FUN_ffff0000907b933c(long param_1,undefined8 param_2,ulong param_3)

{
  int *piVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  undefined2 uVar5;
  int iVar6;
  ulong uVar7;
  undefined4 uVar8;
  long lVar9;
  uint local_3c;
  uint local_38;
  uint local_34;
  
  local_34 = 0;
  local_3c = 0;
  local_38 = 0;
  thunk_FUN_ffff000090707c50(param_2,param_3);
  FUN_ffff00009077ee50(&local_34,0,4);
  FUN_ffff00009077ee50(&local_38,0,4);
  FUN_ffff00009077ee50(&local_3c,0,4);
  lVar9 = *(long *)(param_1 + 0x40);
  uVar7 = thunk_FUN_ffff00009077b334(param_2);
  piVar1 = (int *)(lVar9 + 0x2c);
  do {
    uVar2 = *(uint *)(lVar9 + 0x100);
  } while ((uVar2 & 0xff) < 6);
  uVar3 = 0x30000080;
  if ((param_3 >> 0x1d & 0xffffffff) == 0) {
    uVar3 = 0x30000000;
  }
  uVar3 = uVar3 | (uint)(uVar7 >> 0x20 != 0) << 8;
  if (*(char *)(param_1 + 0x70) == '\0') {
    local_34 = uVar3 | local_34 & 0xc003f67f;
    local_38 = local_38 & 0xdfffffff;
    *(undefined1 *)(param_1 + 0x70) = 1;
  }
  else {
    local_34 = uVar3 | local_34 & 0xc003f67f | 0x1000000;
    local_38 = local_38 | 0x20000000;
  }
  uVar3 = *piVar1 + 1U & 0xffff;
  if ((*piVar1 + 1U & 0xfffe) == 0) {
    uVar3 = 1;
  }
  uVar4 = uVar3 | local_38 & 0x7fff0000;
  if ((local_34 >> 8 & 1) != 0) {
    local_3c = local_3c & 0xfff00000 | local_3c & 0xffff | ((uint)(uVar7 >> 0x20) & 0xf) << 0x10;
  }
  *(uint *)(lVar9 + 0x104) = local_34;
  *(uint *)(lVar9 + 0x104) = uVar4;
  *(uint *)(lVar9 + 0x104) = local_3c;
  *(int *)(lVar9 + 0x104) = (int)(param_3 << 3);
  *(int *)(lVar9 + 0x104) = (int)uVar7;
  if ((char)local_34 < '\0') {
    iVar6 = -6;
    *(int *)(lVar9 + 0x104) = (int)((param_3 & 0x1fffffffffffffff) >> 0x1d);
  }
  else {
    iVar6 = -5;
  }
  *(uint *)(lVar9 + 0x100) = iVar6 + (uVar2 & 0xff);
  local_38._0_2_ = (undefined2)uVar3;
  uVar5 = (undefined2)local_38;
  local_38 = uVar4;
  iVar6 = FUN_ffff0000907bbbcc(piVar1,0xffff,uVar5,5000);
  if (iVar6 == 0) {
    uVar8 = 0;
    if (-1 < *piVar1) {
      uVar8 = 0x724e;
    }
  }
  else {
    uVar8 = 0x7275;
  }
  return uVar8;
}


