
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* IsDeviceUnlocked() */

bool IsDeviceUnlocked(void)

{
  ulong uVar1;
  int *piVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  bool bVar7;
  string *in_x2;
  ushort local_70 [8];
  void *local_60;
  ulong local_58;
  undefined8 local_50;
  char *local_48;
  byte local_40 [8];
  ulong local_38;
  int *local_30;
  long local_28;

  lVar3 = tpidr_el0;
  local_28 = *(long *)(lVar3 + 0x28);
  local_48 = operator_new(0x20);
  local_48[0x19] = '\0';
  uVar6 = s_ro_boot_verifiedbootstate_00148289._17_8_;
  uVar5 = s_ro_boot_verifiedbootstate_00148289._0_8_;
  local_70[0] = 0;
  uVar4 = CONCAT17(s_ro_boot_verifiedbootstate_00148289[0x10],
                   s_ro_boot_verifiedbootstate_00148289._9_7_);
  local_50 = 0x19;
  local_58 = 0x21;
  *(ulong *)(local_48 + 8) =
       CONCAT71(s_ro_boot_verifiedbootstate_00148289._9_7_,s_ro_boot_verifiedbootstate_00148289[8]);
  *(undefined8 *)local_48 = uVar5;
  *(undefined8 *)(local_48 + 0x11) = uVar6;
  *(undefined8 *)(local_48 + 9) = uVar4;
  android::base::GetProperty((base *)&local_58,(string *)local_70,in_x2);
  uVar1 = (ulong)(local_40[0] >> 1);
  if ((local_40[0] & 1) != 0) {
    uVar1 = local_38;
  }
  if (uVar1 == 6) {
    piVar2 = (int *)((ulong)local_40 | 1);
    if ((local_40[0] & 1) != 0) {
      piVar2 = local_30;
    }
    bVar7 = *piVar2 == 0x6e61726f && (short)piVar2[1] == 0x6567;
  }
  else {
    bVar7 = false;
  }
  if ((local_40[0] & 1) != 0) {
    operator_delete(local_30);
  }
  if ((local_70[0] & 1) != 0) {
    operator_delete(local_60);
  }
  if ((local_58 & 1) != 0) {
    operator_delete(local_48);
  }
  if (*(long *)(lVar3 + 0x28) != local_28) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return bVar7;
}
