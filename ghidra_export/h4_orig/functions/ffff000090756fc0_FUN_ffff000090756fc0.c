// ffff000090756fc0  size=500  FUN_ffff000090756fc0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090756fc0(int param_1,long param_2)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  
  if ((param_1 == 0x1b) || (param_1 == 0x15)) {
    if (*(int *)(param_2 + 0xc) != 0) {
      if ((*(char *)(param_2 + 0x2d8) == '\0') || (*(char *)(param_2 + 0x990) == '\0')) {
        lVar4 = (ulong)(param_1 == 0x1b) * 0x10000;
        uVar1 = *(uint *)(param_2 + 0x92c) | *(int *)(param_2 + 0x928) << 0x10;
        *(uint *)(lVar4 + -0xffffcdeaffd0) = uVar1;
        *(uint *)(lVar4 + -0xffffcdeaffcc) = uVar1;
      }
      else {
        uVar1 = *(uint *)(param_2 + 0x92c);
        lVar4 = (ulong)(param_1 == 0x1b) * 0x10000;
        iVar3 = _DAT_ffff00009087b67c - _DAT_ffff00009087b684;
        iVar2 = _DAT_ffff00009087b684 << 0x10;
        if (param_1 != 0x1b) {
          iVar2 = 0;
        }
        *(uint *)(lVar4 + -0xffffcdeaffd0) = uVar1 | _DAT_ffff00009087b67c << 0x10;
        *(uint *)(lVar4 + -0xffffcdeaffcc) = uVar1 | iVar3 * 0x10000;
        *(int *)(lVar4 + -0xffffcdeaffc8) = iVar2;
      }
      *(undefined4 *)(lVar4 + -0xffffcdeb0000) = 1;
      *(undefined4 *)(lVar4 + -0xffffcdeaffe0) = 0x400003;
      *(undefined4 *)(lVar4 + -0xffffcdeafe00) = 0;
      *(undefined4 *)(lVar4 + -0xffffcdeafac4) = 0;
      *(undefined4 *)(lVar4 + -0xffffcdeafa00) = 1;
    }
  }
  else if (*(int *)(param_2 + 0xc) != 0) {
    if (*(char *)(param_2 + 0x2d8) == '\0') {
      lVar4 = (ulong)(param_1 == 0x1a) * 0x10000;
      uVar1 = *(uint *)(param_2 + 0x92c) | *(int *)(param_2 + 0x928) << 0x10;
      *(uint *)(&DAT_ffff000032030020 + lVar4) = *(uint *)(&DAT_ffff000032030020 + lVar4) | 1;
      *(uint *)(&DAT_ffff000032030030 + lVar4) = uVar1;
      *(uint *)(&DAT_ffff0000320304d8 + lVar4) = uVar1;
      *(undefined4 *)(&DAT_ffff000032030000 + lVar4) = 1;
    }
    else if (param_1 == 0x1a) {
      _DAT_ffff000032040030 = *(uint *)(param_2 + 0x92c) | _DAT_ffff00009087b6a0 << 0x10;
      _DAT_ffff000032040020 = _DAT_ffff000032040020 | 1;
      _DAT_ffff0000320404dc = _DAT_ffff00009087b6a8 << 0x10;
      _DAT_ffff0000320404d8 =
           *(uint *)(param_2 + 0x92c) | (_DAT_ffff00009087b6a0 - _DAT_ffff00009087b6a8) * 0x10000;
      _DAT_ffff000032040000 = 1;
    }
    else if (param_1 == 0x14) {
      _DAT_ffff000032030030 = *(uint *)(param_2 + 0x92c) | _DAT_ffff00009087b694 << 0x10;
      _DAT_ffff000032030020 = _DAT_ffff000032030020 | 1;
      _DAT_ffff0000320304d8 =
           *(uint *)(param_2 + 0x92c) | (_DAT_ffff00009087b694 - _DAT_ffff00009087b69c) * 0x10000;
      _DAT_ffff0000320304dc = 0;
      _DAT_ffff000032030000 = 1;
    }
  }
  return 0;
}


