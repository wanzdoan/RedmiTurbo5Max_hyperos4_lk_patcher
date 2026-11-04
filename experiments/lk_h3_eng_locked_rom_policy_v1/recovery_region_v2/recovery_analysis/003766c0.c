
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* copy_demsg() */

undefined4 copy_demsg(void)

{
  int iVar1;
  undefined4 uVar2;
  char *pcVar3;

  puts("========Get last_demsg========");
  iVar1 = access("/dev/block/platform/bootdevice/by-name/minidump",0);
  if (iVar1 == 0) {
    pcVar3 = "/dev/block/platform/bootdevice/by-name/minidump";
  }
  else {
    iVar1 = access("/dev/block/bootdevice/by-name/minidump",0);
    if (iVar1 != 0) {
      uVar2 = copy_demsg_without_minidump();
      goto LAB_00376720;
    }
    pcVar3 = "/dev/block/bootdevice/by-name/minidump";
  }
  uVar2 = copy_demsg_with_minidump(pcVar3);
LAB_00376720:
  puts("==============================");
  return uVar2;
}
