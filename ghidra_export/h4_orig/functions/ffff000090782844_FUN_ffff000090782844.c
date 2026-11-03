// ffff000090782844  size=400  FUN_ffff000090782844

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090782844(void)

{
  int *piVar1;
  uint uVar2;
  uint uVar3;
  undefined4 uVar4;
  char *pcVar5;
  int iVar6;
  
  if (_DAT_ffff0000908870f0 == 0) {
    pcVar5 = s__s__mboot_params_buffer_NULL_ffff0000907e3a11;
  }
  else {
    uVar3 = *(uint *)(_DAT_ffff0000908870f0 + 0x2c);
    if (((uVar3 == 0) ||
        (uVar3 != (*(int *)(_DAT_ffff0000908870f0 + 0x18) + 0x3fU & 0xffffffc0) +
                  *(int *)(_DAT_ffff0000908870f0 + 0x14))) ||
       (*(int *)(_DAT_ffff0000908870f0 + 4) != 0x40)) {
      FUN_ffff000090781ae8
                (s__s__FAILED__off_linux_0x_x__off__ffff0000907e6c2a,
                 s__MBOOT_PARAMS__ffff0000907ed1f2,(ulong)uVar3,
                 *(int *)(_DAT_ffff0000908870f0 + 0x14),*(undefined4 *)(_DAT_ffff0000908870f0 + 4));
      FUN_ffff00009077e11c
                (s__s__FAILED__off_linux_0x_x__off__ffff0000907e6c2a,
                 s__MBOOT_PARAMS__ffff0000907ed1f2,*(undefined4 *)(_DAT_ffff0000908870f0 + 0x2c),
                 *(undefined4 *)(_DAT_ffff0000908870f0 + 0x14),
                 *(undefined4 *)(_DAT_ffff0000908870f0 + 4));
      return;
    }
    piVar1 = (int *)(_DAT_ffff0000908870f0 + (ulong)uVar3);
    uVar4 = *(undefined4 *)(_DAT_ffff0000908870f0 + 0x40);
    iVar6 = *piVar1;
    uVar2 = piVar1[1] ^ 0xaeedead0;
    uVar3 = uVar2;
    if (0xf < uVar2) {
      uVar3 = piVar1[1];
    }
    FUN_ffff000090781ae8
              (s__s__wdt_status_0x_x__fiq_step_0x_ffff0000907e1f33,s__MBOOT_PARAMS__ffff0000907ed1f2
               ,uVar4,iVar6,uVar3);
    FUN_ffff00009077e11c
              (s__s__wdt_status_0x_x__fiq_step_0x_ffff0000907e1f33,s__MBOOT_PARAMS__ffff0000907ed1f2
               ,uVar4,iVar6,uVar3);
    if ((iVar6 != 0) && (0xf < uVar2)) {
      iVar6 = 0;
      *piVar1 = 0;
    }
    pcVar5 = s__s__set_reboot_reason_info_done_ffff0000907db8c5;
    DAT_ffff0000908870f8 = 1;
    _DAT_ffff0000908870fc = uVar4;
    _DAT_ffff000090887100 = iVar6;
    _DAT_ffff000090887104 = uVar3;
  }
  FUN_ffff000090781ae8(pcVar5,s__MBOOT_PARAMS__ffff0000907ed1f2);
  FUN_ffff00009077e11c(pcVar5,s__MBOOT_PARAMS__ffff0000907ed1f2);
  return;
}


