
/* WipeCache(RecoveryUI*, std::function<bool ()> const&) */

uint WipeCache(RecoveryUI *param_1,function *param_2)

{
  char *pcVar1;
  long lVar2;
  uint uVar3;
  long lVar4;
  ulong uVar5;
  string local_50;
  undefined4 local_4f;
  undefined2 local_4b;
  undefined1 local_49;
  void *local_40;
  long local_38;

  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  local_50 = (string)0xc;
  local_4f = 0x6361632f;
  local_4b = 0x6568;
  local_49 = 0;
  lVar4 = volume_for_mount_point(&local_50);
  if (((byte)local_50 & 1) != 0) {
    operator_delete(local_40);
  }
  if (lVar4 == 0) {
    (**(code **)(*(long *)param_1 + 0x158))(param_1,"No /cache partition found.\n");
  }
  else if ((*(long **)(param_2 + 0x20) == (long *)0x0) ||
          (uVar5 = (**(code **)(**(long **)(param_2 + 0x20) + 0x30))(), (uVar5 & 1) != 0)) {
    (**(code **)(*(long *)param_1 + 0x158))(param_1,"\n-- Wiping cache...\n");
    (**(code **)(*(long *)param_1 + 0x118))(param_1,3);
    (**(code **)(*(long *)param_1 + 0x128))(param_1,1);
    uVar3 = EraseVolume("/cache",param_1,false);
    pcVar1 = "complete";
    if ((uVar3 & 1) == 0) {
      pcVar1 = "failed";
    }
    (**(code **)(*(long *)param_1 + 0x158))(param_1,"Cache wipe %s.\n",pcVar1);
    goto LAB_0026fe48;
  }
  uVar3 = 0;
LAB_0026fe48:
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return uVar3 & 1;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
