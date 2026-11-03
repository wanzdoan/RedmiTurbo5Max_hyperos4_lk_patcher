// ffff000090756d68  size=332  FUN_ffff000090756d68

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090756d68(int param_1,long param_2)

{
  int iVar1;
  uint *puVar2;
  uint uVar3;
  long lVar4;
  
  if (*(int *)(param_2 + 0xc) != 0) {
    if (*(char *)(param_2 + 0x2d8) == '\0') {
      iVar1 = *(int *)(param_2 + 0x928);
      uVar3 = *(uint *)(param_2 + 0x92c);
      if (param_1 == 0x21) {
        lVar4 = 0x20000;
      }
      else if (param_1 == 0x20) {
        lVar4 = 0x30000;
      }
      else if (param_1 == 0x13) {
        lVar4 = 0x10000;
      }
      else {
        lVar4 = 0;
      }
      *(uint *)(&DAT_ffff000032090020 + lVar4) = *(uint *)(&DAT_ffff000032090020 + lVar4) | 1;
      *(uint *)(&DAT_ffff000032090030 + lVar4) = uVar3 | iVar1 << 0x10;
      *(undefined4 *)(&DAT_ffff000032090000 + lVar4) = 1;
    }
    else {
      switch(param_1) {
      case 0xe:
        uVar3 = *(uint *)(param_2 + 0x92c);
        puVar2 = (uint *)&DAT_ffff000032090020;
        iVar1 = _DAT_ffff00009087b634;
        break;
      default:
        goto code_r0xffff000090756ea8;
      case 0x13:
        uVar3 = *(uint *)(param_2 + 0x92c);
        puVar2 = (uint *)&DAT_ffff0000320a0020;
        iVar1 = _DAT_ffff00009087b64c;
        break;
      case 0x20:
        uVar3 = *(uint *)(param_2 + 0x92c);
        puVar2 = (uint *)&DAT_ffff0000320c0020;
        iVar1 = _DAT_ffff00009087b658;
        break;
      case 0x21:
        uVar3 = *(uint *)(param_2 + 0x92c);
        puVar2 = (uint *)&DAT_ffff0000320b0020;
        iVar1 = _DAT_ffff00009087b640;
      }
      *puVar2 = *puVar2 | 1;
      puVar2[4] = uVar3 | iVar1 << 0x10;
      puVar2[-8] = 1;
    }
  }
code_r0xffff000090756ea8:
  return 0;
}


