
void FUN_ffff0000be064948(void)

{
  int *piVar1;
  uint uVar2;
  uint uVar3;
  undefined4 uVar4;
  char *pcVar5;
  int iVar6;
  
  if (DAT_ffff0000be1321c0 == 0) {
    pcVar5 = s__s__mboot_params_buffer_NULL_ffff0000be0b14be;
  }
  else {
    uVar3 = *(uint *)(DAT_ffff0000be1321c0 + 0x2c);
    if (((uVar3 == 0) ||
        (uVar3 != (*(int *)(DAT_ffff0000be1321c0 + 0x18) + 0x3fU & 0xffffffc0) +
                  *(int *)(DAT_ffff0000be1321c0 + 0x14))) ||
       (*(int *)(DAT_ffff0000be1321c0 + 4) != 0x40)) {
      FUN_ffff0000be063b40
                (s__s__FAILED__off_linux_0x_x__off__ffff0000be0b3568,
                 s__MBOOT_PARAMS__ffff0000be0b7f52,(ulong)uVar3,
                 *(int *)(DAT_ffff0000be1321c0 + 0x14),*(undefined4 *)(DAT_ffff0000be1321c0 + 4));
      FUN_ffff0000be05fd98
                (s__s__FAILED__off_linux_0x_x__off__ffff0000be0b3568,
                 s__MBOOT_PARAMS__ffff0000be0b7f52,*(undefined4 *)(DAT_ffff0000be1321c0 + 0x2c),
                 *(undefined4 *)(DAT_ffff0000be1321c0 + 0x14),
                 *(undefined4 *)(DAT_ffff0000be1321c0 + 4));
      return;
    }
    piVar1 = (int *)(DAT_ffff0000be1321c0 + (ulong)uVar3);
    uVar4 = *(undefined4 *)(DAT_ffff0000be1321c0 + 0x40);
    iVar6 = *piVar1;
    uVar2 = piVar1[1] ^ 0xaeedead0;
    uVar3 = uVar2;
    if (0xf < uVar2) {
      uVar3 = piVar1[1];
    }
    FUN_ffff0000be063b40
              (s__s__wdt_status_0x_x__fiq_step_0x_ffff0000be0b048d,s__MBOOT_PARAMS__ffff0000be0b7f52
               ,uVar4,iVar6,uVar3);
    FUN_ffff0000be05fd98
              (s__s__wdt_status_0x_x__fiq_step_0x_ffff0000be0b048d,s__MBOOT_PARAMS__ffff0000be0b7f52
               ,uVar4,iVar6,uVar3);
    if ((iVar6 != 0) && (0xf < uVar2)) {
      iVar6 = 0;
      *piVar1 = 0;
    }
    pcVar5 = s__s__set_reboot_reason_info_done_ffff0000be0abe19;
    DAT_ffff0000be1321c8 = 1;
    DAT_ffff0000be1321cc = uVar4;
    DAT_ffff0000be1321d0 = iVar6;
    DAT_ffff0000be1321d4 = uVar3;
  }
  FUN_ffff0000be063b40(pcVar5,s__MBOOT_PARAMS__ffff0000be0b7f52);
  FUN_ffff0000be05fd98(pcVar5,s__MBOOT_PARAMS__ffff0000be0b7f52);
  return;
}


