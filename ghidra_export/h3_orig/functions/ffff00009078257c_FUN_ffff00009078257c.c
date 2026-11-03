// ffff00009078257c  size=400  FUN_ffff00009078257c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078257c(void)

{
  int *piVar1;
  uint uVar2;
  uint uVar3;
  undefined4 uVar4;
  char *pcVar5;
  int iVar6;
  
  if (_DAT_ffff000090887058 == 0) {
    pcVar5 = s__s__mboot_params_buffer_NULL_ffff0000907e3940;
  }
  else {
    uVar3 = *(uint *)(_DAT_ffff000090887058 + 0x2c);
    if (((uVar3 == 0) ||
        (uVar3 != (*(int *)(_DAT_ffff000090887058 + 0x18) + 0x3fU & 0xffffffc0) +
                  *(int *)(_DAT_ffff000090887058 + 0x14))) ||
       (*(int *)(_DAT_ffff000090887058 + 4) != 0x40)) {
      FUN_ffff000090781820
                (s__s__FAILED__off_linux_0x_x__off__ffff0000907e6b16,
                 s__MBOOT_PARAMS__ffff0000907ed06f,(ulong)uVar3,
                 *(int *)(_DAT_ffff000090887058 + 0x14),*(undefined4 *)(_DAT_ffff000090887058 + 4));
      FUN_ffff00009077de54
                (s__s__FAILED__off_linux_0x_x__off__ffff0000907e6b16,
                 s__MBOOT_PARAMS__ffff0000907ed06f,*(undefined4 *)(_DAT_ffff000090887058 + 0x2c),
                 *(undefined4 *)(_DAT_ffff000090887058 + 0x14),
                 *(undefined4 *)(_DAT_ffff000090887058 + 4));
      return;
    }
    piVar1 = (int *)(_DAT_ffff000090887058 + (ulong)uVar3);
    uVar4 = *(undefined4 *)(_DAT_ffff000090887058 + 0x40);
    iVar6 = *piVar1;
    uVar2 = piVar1[1] ^ 0xaeedead0;
    uVar3 = uVar2;
    if (0xf < uVar2) {
      uVar3 = piVar1[1];
    }
    FUN_ffff000090781820
              (s__s__wdt_status_0x_x__fiq_step_0x_ffff0000907e1e62,s__MBOOT_PARAMS__ffff0000907ed06f
               ,uVar4,iVar6,uVar3);
    FUN_ffff00009077de54
              (s__s__wdt_status_0x_x__fiq_step_0x_ffff0000907e1e62,s__MBOOT_PARAMS__ffff0000907ed06f
               ,uVar4,iVar6,uVar3);
    if ((iVar6 != 0) && (0xf < uVar2)) {
      iVar6 = 0;
      *piVar1 = 0;
    }
    pcVar5 = s__s__set_reboot_reason_info_done_ffff0000907db7ba;
    DAT_ffff000090887060 = 1;
    _DAT_ffff000090887064 = uVar4;
    _DAT_ffff000090887068 = iVar6;
    _DAT_ffff00009088706c = uVar3;
  }
  FUN_ffff000090781820(pcVar5,s__MBOOT_PARAMS__ffff0000907ed06f);
  FUN_ffff00009077de54(pcVar5,s__MBOOT_PARAMS__ffff0000907ed06f);
  return;
}


